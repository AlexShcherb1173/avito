package ru.avito.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.avito.entity.Ad;
import ru.avito.entity.Role;
import ru.avito.entity.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _AdRepositoryT {

    @Container
    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("avito_test")
            .withUsername("test")
            .withPassword("test");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
    }

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void cleanDatabase() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindAllAdsByAuthorId() {
        User firstUser = saveUser("user1@example.com", "+79990000001");
        User secondUser = saveUser("user2@example.com", "+79990000002");

        Ad firstUserAd1 = saveAd(firstUser, "First user ad 1", 10000, "First user ad 1 description");
        Ad firstUserAd2 = saveAd(firstUser, "First user ad 2", 20000, "First user ad 2 description");
        saveAd(secondUser, "Second user ad", 30000, "Second user ad description");

        List<Ad> result = adRepository.findAllByAuthorId(firstUser.getId());

        assertThat(result)
                .hasSize(2)
                .extracting(Ad::getId)
                .containsExactlyInAnyOrder(firstUserAd1.getId(), firstUserAd2.getId());

        assertThat(result)
                .extracting(ad -> ad.getAuthor().getId())
                .containsOnly(firstUser.getId());
    }

    @Test
    void shouldReturnEmptyListWhenAuthorHasNoAds() {
        User user = saveUser("user@example.com", "+79990000001");

        List<Ad> result = adRepository.findAllByAuthorId(user.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldPersistAndLoadAd() {
        User author = saveUser("user@example.com", "+79990000001");

        Ad ad = saveAd(author, "Persisted ad", 15000, "Persisted ad description");

        entityManager.clear();

        Ad reloadedAd = adRepository.findById(ad.getId()).orElseThrow();

        assertThat(reloadedAd.getId()).isEqualTo(ad.getId());
        assertThat(reloadedAd.getTitle()).isEqualTo("Persisted ad");
        assertThat(reloadedAd.getPrice()).isEqualTo(15000);
        assertThat(reloadedAd.getDescription()).isEqualTo("Persisted ad description");
        assertThat(reloadedAd.getAuthor().getId()).isEqualTo(author.getId());
    }

    @Test
    void shouldCascadeDeleteAdsWhenAuthorDeleted() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Cascade ad", 12000, "Cascade ad description");

        userRepository.delete(author);
        userRepository.flush();

        assertThat(adRepository.findById(ad.getId())).isEmpty();
    }

    @Test
    void shouldEnforceForeignKeyForAuthor() {
        Ad ad = Ad.builder()
                .title("Broken ad")
                .price(10000)
                .description("Broken ad description")
                .author(User.builder().id(999999).build())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            adRepository.saveAndFlush(ad);
        });
    }

    @Test
    void shouldRequireMandatoryFields() {
        User author = saveUser("user@example.com", "+79990000001");

        Ad ad = Ad.builder()
                .title(null)
                .price(null)
                .description(null)
                .author(author)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            adRepository.saveAndFlush(ad);
        });
    }

    private User saveUser(String email, String phone) {
        User user = User.builder()
                .email(email)
                .password("encoded-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone(phone)
                .role(Role.USER)
                .build();

        return userRepository.saveAndFlush(user);
    }

    private Ad saveAd(User author, String title, Integer price, String description) {
        Ad ad = Ad.builder()
                .title(title)
                .price(price)
                .description(description)
                .image(null)
                .author(author)
                .build();

        return adRepository.saveAndFlush(ad);
    }
}