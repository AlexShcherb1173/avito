package ru.skypro.homework.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getComments_ShouldReturnCommentsDTO_WhenCommentsExist() {
        // Arrange
        Integer adId = 1;
        Comment comment = new Comment();
        comment.setText("Test comment");
        when(commentRepository.findAllByAdId(adId)).thenReturn(Collections.singletonList(comment));
        when(commentMapper.toDtoCommentsDTO(1, Collections.singletonList(comment))).thenReturn(new CommentsDTO(1, Collections.singletonList(new CommentDTO())));

        // Act
        CommentsDTO result = commentService.getComments(adId);

        // Assert
        assertNotNull(result);
        verify(commentRepository).findAllByAdId(adId);
    }

    @Test
    void addComment_ShouldReturnCommentDTO_WhenCommentIsAdded() {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateCommentDTO createCommentDTO = new CreateOrUpdateCommentDTO();
        createCommentDTO.setText("New comment");
        Comment comment = new Comment();
        comment.setText("New comment");
        Comment savedComment = new Comment();
        savedComment.setId(1);
        savedComment.setText("New comment");

        // Настройка маппинга
        when(commentMapper.toModel(createCommentDTO)).thenReturn(comment);
        when(adRepository.findById(adId)).thenReturn(Optional.of(new Ad()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toDtoCommentDTO(savedComment)).thenReturn(new CommentDTO());

        // Установка контекста безопасности
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("testUser").password("password").roles("USER").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        CommentDTO result = commentService.addComment(adId, createCommentDTO);

        // Assert
        assertNotNull(result);
        verify(commentRepository).save(comment);
    }

    @Test
    void addComment_ShouldThrowException_WhenAdNotFound() {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateCommentDTO createCommentDTO = new CreateOrUpdateCommentDTO();
        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.addComment(adId, createCommentDTO));
    }

    @Test
    void deleteComment_ShouldDeleteComment_WhenCommentExists() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(adId, commentId);

        // Assert
        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_ShouldThrowCommentNotFoundException_WhenCommentDoesNotExist() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(adId, commentId));
    }

    @Test
    void updateComment_ShouldReturnUpdatedCommentDTO_WhenCommentIsUpdated() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        CreateOrUpdateCommentDTO updateCommentDTO = new CreateOrUpdateCommentDTO();
        updateCommentDTO.setText("Updated comment");
        Comment existingComment = new Comment();
        existingComment.setText("Old comment");
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(existingComment);
        when(commentMapper.toDtoCommentDTO(existingComment)).thenReturn(new CommentDTO());

        // Act
        CommentDTO result = commentService.updateComment(adId, commentId, updateCommentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated comment", existingComment.getText());
        verify(commentRepository).save(existingComment);
    }

    @Test
    void updateComment_ShouldThrowCommentNotFoundException_WhenCommentDoesNotExist() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        CreateOrUpdateCommentDTO updateCommentDTO = new CreateOrUpdateCommentDTO();
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> commentService.updateComment(adId, commentId, updateCommentDTO));
    }
}