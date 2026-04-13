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
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class _CommentRepositoryT {

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
    private CommentRepository commentRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindCommentsByAdIdOrderedByCreatedAtAsc() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Ad title", 10000, "Ad description");

        Comment laterComment = saveComment(author, ad, "Later comment text", Instant.parse("2026-03-29T10:15:30Z"));
        Comment earlierComment = saveComment(author, ad, "Earlier comment text", Instant.parse("2026-03-29T09:15:30Z"));
        Comment middleComment = saveComment(author, ad, "Middle comment text", Instant.parse("2026-03-29T09:45:30Z"));

        List<Comment> result = commentRepository.findAllByAdIdOrderByCreatedAtAsc(ad.getId());

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Comment::getId)
                .containsExactly(
                        earlierComment.getId(),
                        middleComment.getId(),
                        laterComment.getId()
                );
    }

    @Test
    void shouldReturnOnlyCommentsForRequestedAd() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad firstAd = saveAd(author, "First ad", 10000, "First ad description");
        Ad secondAd = saveAd(author, "Second ad", 20000, "Second ad description");

        Comment firstAdComment = saveComment(author, firstAd, "First ad comment", Instant.parse("2026-03-29T09:00:00Z"));
        saveComment(author, secondAd, "Second ad comment", Instant.parse("2026-03-29T10:00:00Z"));

        List<Comment> result = commentRepository.findAllByAdIdOrderByCreatedAtAsc(firstAd.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(firstAdComment.getId());
        assertThat(result.get(0).getAd().getId()).isEqualTo(firstAd.getId());
    }

    @Test
    void shouldReturnEmptyListWhenAdHasNoComments() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Ad without comments", 10000, "Ad without comments description");

        List<Comment> result = commentRepository.findAllByAdIdOrderByCreatedAtAsc(ad.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCascadeDeleteCommentsWhenAdDeleted() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Ad title", 10000, "Ad description");
        Comment comment = saveComment(author, ad, "Comment text", Instant.parse("2026-03-29T09:00:00Z"));

        adRepository.delete(ad);
        adRepository.flush();

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void shouldCascadeDeleteCommentsWhenAuthorDeleted() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Ad title", 10000, "Ad description");
        Comment comment = saveComment(author, ad, "Comment text", Instant.parse("2026-03-29T09:00:00Z"));

        userRepository.delete(author);
        userRepository.flush();

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void shouldEnforceForeignKeysForComment() {
        Comment comment = Comment.builder()
                .text("Broken comment text")
                .createdAt(Instant.now())
                .author(User.builder().id(999999).build())
                .ad(Ad.builder().id(999999).build())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            commentRepository.saveAndFlush(comment);
        });
    }

    @Test
    void shouldRequireMandatoryFields() {
        User author = saveUser("user@example.com", "+79990000001");
        Ad ad = saveAd(author, "Ad title", 10000, "Ad description");

        Comment comment = Comment.builder()
                .text(null)
                .createdAt(null)
                .author(author)
                .ad(ad)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            commentRepository.saveAndFlush(comment);
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

    private Comment saveComment(User author, Ad ad, String text, Instant createdAt) {
        Comment comment = Comment.builder()
                .text(text)
                .createdAt(createdAt)
                .author(author)
                .ad(ad)
                .build();

        return commentRepository.saveAndFlush(comment);
    }
}