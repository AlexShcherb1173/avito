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
import ru.avito.entity.Role;
import ru.avito.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _UserRepositoryT {

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
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void cleanDatabase() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserByEmail() {
        User savedUser = saveUser("user@example.com", "+79990000001");

        Optional<User> result = userRepository.findByEmail("user@example.com");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getId()).isEqualTo(savedUser.getId());
        assertThat(result.orElseThrow().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("missing@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldEnforceUniqueEmail() {
        saveUser("unique@example.com", "+79990000001");

        User duplicate = User.builder()
                .email("unique@example.com")
                .password("encoded-password")
                .firstName("Petr")
                .lastName("Petrov")
                .phone("+79990000002")
                .role(Role.USER)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    void shouldRequireMandatoryFields() {
        User user = User.builder()
                .email(null)
                .password(null)
                .firstName(null)
                .lastName(null)
                .phone(null)
                .role(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    void shouldPersistRoleAndImage() {
        User user = User.builder()
                .email("admin@example.com")
                .password("encoded-password")
                .firstName("Admin")
                .lastName("User")
                .phone("+79990000003")
                .role(Role.ADMIN)
                .image("/images/users/1/avatar.jpg")
                .build();

        User savedUser = userRepository.saveAndFlush(user);

        Optional<User> result = userRepository.findById(savedUser.getId());

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getRole()).isEqualTo(Role.ADMIN);
        assertThat(result.orElseThrow().getImage()).isEqualTo("/images/users/1/avatar.jpg");
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
}