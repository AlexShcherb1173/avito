package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.avito.dto.comment.CommentDto;
import ru.avito.dto.comment.CommentsResponse;
import ru.avito.dto.comment.CreateOrUpdateCommentRequest;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.CommentMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.CommentRepository;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.impl.CommentServiceImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AccessService accessService;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private User admin;
    private Ad ad;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("user@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .build();

        admin = User.builder()
                .id(2)
                .email("admin@example.com")
                .role(Role.ADMIN)
                .build();

        ad = Ad.builder()
                .id(10)
                .title("Ad title")
                .price(10000)
                .description("Ad description")
                .author(user)
                .build();

        comment = Comment.builder()
                .id(20)
                .text("Old comment text")
                .createdAt(Instant.now())
                .author(user)
                .ad(ad)
                .build();
    }

    @Test
    void shouldGetComments() {
        Comment secondComment = Comment.builder()
                .id(21)
                .text("Second comment text")
                .createdAt(Instant.now())
                .author(user)
                .ad(ad)
                .build();

        CommentDto firstDto = new CommentDto();
        firstDto.setPk(20);
        firstDto.setText("Old comment text");

        CommentDto secondDto = new CommentDto();
        secondDto.setPk(21);
        secondDto.setText("Second comment text");

        when(adRepository.existsById(10)).thenReturn(true);
        when(commentRepository.findAllByAdIdOrderByCreatedAtAsc(10)).thenReturn(List.of(comment, secondComment));
        when(commentMapper.toDto(comment)).thenReturn(firstDto);
        when(commentMapper.toDto(secondComment)).thenReturn(secondDto);

        CommentsResponse result = commentService.getComments(10);

        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getResults()).hasSize(2);
        verify(adRepository).existsById(10);
        verify(commentRepository).findAllByAdIdOrderByCreatedAtAsc(10);
    }

    @Test
    void shouldThrowWhenAdNotFoundForGetComments() {
        when(adRepository.existsById(999)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> commentService.getComments(999));
    }

    @Test
    void shouldAddComment() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Created comment text");

        Comment savedComment = Comment.builder()
                .id(25)
                .text("Created comment text")
                .createdAt(Instant.now())
                .author(user)
                .ad(ad)
                .build();

        CommentDto mappedDto = new CommentDto();
        mappedDto.setPk(25);
        mappedDto.setText("Created comment text");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(adRepository.findById(10)).thenReturn(Optional.of(ad));
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
            when(commentMapper.toDto(savedComment)).thenReturn(mappedDto);

            CommentDto result = commentService.addComment(10, request);

            verify(commentRepository).save(any(Comment.class));
            assertThat(result.getPk()).isEqualTo(25);
            assertThat(result.getText()).isEqualTo("Created comment text");
        }
    }

    @Test
    void shouldThrowWhenAdNotFoundForAddComment() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Created comment text");

        when(adRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.addComment(999, request));
    }

    @Test
    void shouldUpdateComment() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        CommentDto mappedDto = new CommentDto();
        mappedDto.setPk(20);
        mappedDto.setText("Updated comment text");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(commentRepository.save(comment)).thenReturn(comment);
            when(commentMapper.toDto(comment)).thenReturn(mappedDto);

            CommentDto result = commentService.updateComment(10, 20, request);

            verify(accessService).checkCommentEditAccess(user, comment);
            verify(commentRepository).save(comment);
            assertThat(comment.getText()).isEqualTo("Updated comment text");
            assertThat(result.getText()).isEqualTo("Updated comment text");
        }
    }

    @Test
    void shouldThrowWhenCommentNotFoundForUpdate() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        when(commentRepository.findByIdAndAdId(999, 10)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.updateComment(10, 999, request));
    }

    @Test
    void shouldDeleteComment() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

            commentService.deleteComment(10, 20);

            verify(accessService).checkCommentDeleteAccess(user, comment);
            verify(commentRepository).delete(comment);
        }
    }

    @Test
    void shouldThrowWhenCommentNotFoundForDelete() {
        when(commentRepository.findByIdAndAdId(999, 10)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.deleteComment(10, 999));
    }

    @Test
    void shouldThrowWhenAuthenticatedUserNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");

            when(adRepository.findById(10)).thenReturn(Optional.of(ad));
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
            request.setText("Created comment text");

            assertThrows(NotFoundException.class, () -> commentService.addComment(10, request));
        }
    }

    @Test
    void shouldThrowWhenAuthenticatedUserNotFoundForUpdateComment() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> commentService.updateComment(10, 20, request));
        }
    }

    @Test
    void shouldThrowWhenAuthenticatedUserNotFoundForDeleteComment() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> commentService.deleteComment(10, 20));
        }
    }

    @Test
    void shouldNotSaveWhenAccessServiceThrowsOnUpdate() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            doThrow(new RuntimeException("forbidden")).when(accessService).checkCommentEditAccess(user, comment);

            assertThrows(RuntimeException.class, () -> commentService.updateComment(10, 20, request));
            verify(commentRepository, never()).save(any(Comment.class));
        }
    }

    @Test
    void shouldNotDeleteWhenAccessServiceThrowsOnDelete() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(commentRepository.findByIdAndAdId(20, 10)).thenReturn(Optional.of(comment));
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            doThrow(new RuntimeException("forbidden")).when(accessService).checkCommentDeleteAccess(user, comment);

            assertThrows(RuntimeException.class, () -> commentService.deleteComment(10, 20));
            verify(commentRepository, never()).delete(any(Comment.class));
        }
    }
}