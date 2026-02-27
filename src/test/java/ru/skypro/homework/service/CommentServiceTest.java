package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired private CommentService commentService;
    @Autowired private UserRepository userRepository;
    @Autowired private AdRepository adRepository;
    @Autowired private CommentRepository commentRepository;

    private UserEntity adOwner;
    private UserEntity commentOwner;
    private UserEntity otherUser;
    private UserEntity admin;
    private AdEntity ad;
    private CommentEntity comment;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();

        adOwner = userRepository.save(UserEntity.builder()
                .email("adOwner@mail.com")
                .password("x")
                .firstName("Ad")
                .lastName("Owner")
                .role(UserRole.USER)
                .build());

        commentOwner = userRepository.save(UserEntity.builder()
                .email("commentOwner@mail.com")
                .password("x")
                .firstName("Comment")
                .lastName("Owner")
                .role(UserRole.USER)
                .build());

        otherUser = userRepository.save(UserEntity.builder()
                .email("other@mail.com")
                .password("x")
                .firstName("Other")
                .lastName("User")
                .role(UserRole.USER)
                .build());

        admin = userRepository.save(UserEntity.builder()
                .email("admin@mail.com")
                .password("x")
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build());

        ad = adRepository.save(AdEntity.builder()
                .title("T")
                .description("D")
                .price(1)
                .author(adOwner)
                .build());

        comment = commentRepository.save(CommentEntity.builder()
                .text("old")
                .createdAt(Instant.now())
                .ad(ad)
                .author(commentOwner)
                .build());
    }

    @Test
    void updateComment_whenNotOwnerAndNotAdmin_shouldThrowForbidden() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("new");

        assertThatThrownBy(() -> commentService.updateComment(ad.getId(), comment.getId(), otherUser.getEmail(), dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("403");
    }

    @Test
    void updateComment_whenAdmin_shouldAllow() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("admin-update");

        var updated = commentService.updateComment(ad.getId(), comment.getId(), admin.getEmail(), dto);

        assertThat(updated.getText()).isEqualTo("admin-update");
    }

    @Test
    void deleteComment_whenOwner_shouldDelete() {
        commentService.deleteComment(ad.getId(), comment.getId(), commentOwner.getEmail());
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    void deleteComment_whenNotOwner_shouldThrowForbidden() {
        assertThatThrownBy(() -> commentService.deleteComment(ad.getId(), comment.getId(), otherUser.getEmail()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("403");
        assertThat(commentRepository.findById(comment.getId())).isPresent();
    }
}

