package ru.skypro.homework.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommentIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_COMMENT_TEXT = "Test comment text";
    private static final String UPDATED_COMMENT_TEXT = "Updated comment text";

    private UserEntity testUser;
    private AdEntity testAd;
    private CommentEntity testComment;

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем тестового пользователя
        testUser = new UserEntity();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("password");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("89140001122");
        testUser.setRole(Role.USER);
        testUser.setImage("avatar_test_image.jpg");
        userRepository.save(testUser);

        // Создаем тестовое объявление
        testAd = new AdEntity();
        testAd.setTitle("Test Ad");
        testAd.setPrice(1000);
        testAd.setDescription("Test Description");
        testAd.setAuthor(testUser);
        testAd.setImage("ad_image_test.jpg");
        adRepository.save(testAd);

        // Создаем тестовый комментарий
        testComment = new CommentEntity();
        testComment.setText(TEST_COMMENT_TEXT);
        testComment.setAuthor(testUser);
        testComment.setAd(testAd);
        testComment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(testComment);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getComments_ShouldReturnCommentsForAd() {
        // When
        CommentsDto result = commentService.getComments(testAd.getId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        assertEquals(TEST_COMMENT_TEXT, result.getResults().get(0).getText());
        assertEquals(testUser.getId(), result.getResults().get(0).getAuthor());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getComments_WhenAdNotExists_ShouldThrowException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () ->
                commentService.getComments(999));
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void createComment_ShouldCreateNewComment() {
        // Given
        CreateOrUpdateCommentDto createDto = new CreateOrUpdateCommentDto();
        createDto.setText("New comment text");

        // When
        CommentDto result = commentService.createComment(testAd.getId(), createDto, TEST_EMAIL);

        // Then
        assertNotNull(result);
        assertEquals("New comment text", result.getText());
        assertEquals(testUser.getId(), result.getAuthor());
        assertEquals("Иван", result.getAuthorFirstName());

        // Проверяем, что комментарий сохранился в базе
        List<CommentEntity> comments = commentRepository.findByAdId(testAd.getId());
        assertEquals(2, comments.size());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateComment_ShouldUpdateComment() {
        // Given
        CreateOrUpdateCommentDto updateDto = new CreateOrUpdateCommentDto();
        updateDto.setText(UPDATED_COMMENT_TEXT);

        // When
        CommentDto result = commentService.updateComment(
                testAd.getId(), testComment.getId(), updateDto, TEST_EMAIL);

        // Then
        assertNotNull(result);
        assertEquals(UPDATED_COMMENT_TEXT, result.getText());

        // Проверяем, что в базе обновилось
        CommentEntity updatedComment = commentRepository.findById(testComment.getId()).orElseThrow();
        assertEquals(UPDATED_COMMENT_TEXT, updatedComment.getText());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void deleteComment_ShouldDeleteComment() {
        // When
        commentService.deleteComment(testAd.getId(), testComment.getId(), TEST_EMAIL);

        // Then
        assertFalse(commentRepository.existsById(testComment.getId()));
        assertEquals(0, commentRepository.findByAdId(testAd.getId()).size());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void deleteComment_WhenCommentNotExists_ShouldThrowException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () ->
                commentService.deleteComment(testAd.getId(), 999, TEST_EMAIL));
    }
}
