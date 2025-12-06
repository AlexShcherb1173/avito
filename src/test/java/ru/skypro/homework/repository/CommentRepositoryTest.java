package ru.skypro.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;
    private AdEntity testAd;
    private AdEntity anotherAd;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new UserEntity();
        testUser.setEmail("user@example.com");
        testUser.setPassword("password");
        testUser.setFirstName("Иван");
        testUser.setLastName("Ivanov");
        testUser.setPhone("89140001122");
        testUser.setRole(Role.USER);
        testUser.setImage("avatar_test_image.jpeg");
        testUser = userRepository.save(testUser);

        testAd = new AdEntity();
        testAd.setTitle("Test Ad 1");
        testAd.setPrice(1000);
        testAd.setDescription("Test Description1");
        testAd.setImage("ad_image_test1.jpeg");
        testAd.setAuthor(testUser);
        testAd = adRepository.save(testAd);

        anotherAd = new AdEntity();
        anotherAd.setTitle("Test Ad 2");
        anotherAd.setPrice(2000);
        anotherAd.setDescription("Test Description2");
        anotherAd.setImage("ad_image_test2.jpeg");
        anotherAd.setAuthor(testUser);
        anotherAd = adRepository.save(anotherAd);
    }

    @Test
    void saveComment_ShouldPersistComment() {
        // Given
        CommentEntity comment = new CommentEntity();
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setAuthor(testUser);
        comment.setAd(testAd);

        // When
        CommentEntity savedComment = commentRepository.save(comment);

        // Then
        assertNotNull(savedComment.getId());
        assertEquals("Test comment", savedComment.getText());
        assertEquals(testUser.getId(), savedComment.getAuthor().getId());
        // assertEquals(testUser, savedComment.getAuthor());
        assertEquals(testAd.getId(), savedComment.getAd().getId());
        // assertEquals(testAd, savedComment.getAd());
        assertNotNull(savedComment.getCreatedAt());
        assertTrue(commentRepository.existsById(savedComment.getId()));
    }

    @Test
    void findByAdId_ShouldReturnCommentsForSpecificAd() {
        // Given
        CommentEntity comment1 = new CommentEntity();
        comment1.setText("Comment 1");
        comment1.setAuthor(testUser);
        comment1.setAd(testAd);
        comment1.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment1);

        CommentEntity comment2 = new CommentEntity();
        comment2.setText("Comment 2");
        comment2.setAuthor(testUser);
        comment2.setAd(testAd);
        comment2.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment2);

        CommentEntity comment3 = new CommentEntity();
        comment3.setText("Comment for another ad");
        comment3.setAuthor(testUser);
        comment3.setAd(anotherAd);
        comment3.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment3);

        // When
        List<CommentEntity> comments = commentRepository.findByAdId(testAd.getId());

        // Then
        assertEquals(2, comments.size());
        assertTrue(comments.stream().allMatch(c -> c.getAd().getId().equals(testAd.getId())));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 1")));
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("Comment 2")));
    }

    @Test
    void findByAdId_WhenNoComments_ShouldReturnEmptyList() {
        // Given - объявление без комментариев

        // When
        List<CommentEntity> comments = commentRepository.findByAdId(testAd.getId());

        // Then
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void deleteByAdId_ShouldDeleteAllCommentsForAd() {
        // Given
        CommentEntity comment1 = new CommentEntity();
        comment1.setText("Comment 1");
        comment1.setAuthor(testUser);
        comment1.setAd(testAd);
        commentRepository.save(comment1);

        CommentEntity comment2 = new CommentEntity();
        comment2.setText("Comment 2");
        comment2.setAuthor(testUser);
        comment2.setAd(testAd);
        commentRepository.save(comment2);

        CommentEntity comment3 = new CommentEntity();
        comment3.setText("Comment for another ad");
        comment3.setAuthor(testUser);
        comment3.setAd(anotherAd);
        commentRepository.save(comment3);

        // When
        commentRepository.deleteByAdId(testAd.getId());

        // Then
        List<CommentEntity> remainingComments = commentRepository.findAll();
        assertEquals(1, remainingComments.size());
        assertEquals("Comment for another ad", remainingComments.get(0).getText());
    }

    @Test
    void findById_WhenCommentExists_ShouldReturnComment() {
        // Given
        CommentEntity comment = new CommentEntity();
        comment.setText("Test comment");
        comment.setAuthor(testUser);
        comment.setAd(testAd);
        CommentEntity savedComment = commentRepository.save(comment);

        // When
        CommentEntity foundComment = commentRepository.findById(savedComment.getId()).orElse(null);

        // Then
        assertNotNull(foundComment);
        assertEquals(savedComment.getId(), foundComment.getId());
        assertEquals("Test comment", foundComment.getText());
        assertEquals(testUser, foundComment.getAuthor());
        assertEquals(testAd, foundComment.getAd());
    }

    @Test
    void findAll_ShouldReturnAllComments() {
        // Given
        CommentEntity comment1 = new CommentEntity();
        comment1.setText("Comment 1");
        comment1.setAuthor(testUser);
        comment1.setAd(testAd);
        commentRepository.save(comment1);

        CommentEntity comment2 = new CommentEntity();
        comment2.setText("Comment 2");
        comment2.setAuthor(testUser);
        comment2.setAd(anotherAd);
        commentRepository.save(comment2);

        // When
        List<CommentEntity> allComments = commentRepository.findAll();

        // Then
        assertEquals(2, allComments.size());
    }

    @Test
    void deleteById_ShouldRemoveComment() {
        // Given
        CommentEntity comment = new CommentEntity();
        comment.setText("Test comment");
        comment.setAuthor(testUser);
        comment.setAd(testAd);
        CommentEntity savedComment = commentRepository.save(comment);

        // When
        commentRepository.deleteById(savedComment.getId());

        // Then
        assertFalse(commentRepository.existsById(savedComment.getId()));
    }
}
