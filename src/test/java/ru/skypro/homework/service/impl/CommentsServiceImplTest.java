package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserService userService;

    @Mock
    private AdsService adsService;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    @Test
    void getComments_ShouldReturnComments_WhenCommentsExist() {
        // Given
        Integer adId = 1;
        CommentEntity commentEntity1 = new CommentEntity();
        commentEntity1.setId(1);
        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setId(2);
        List<CommentEntity> commentEntities = Arrays.asList(commentEntity1, commentEntity2);

        Comment comment1 = new Comment();
        comment1.setPk(1);
        Comment comment2 = new Comment();
        comment2.setPk(2);

        when(commentRepository.findAllByAdIdOrderByCreatedAtDesc(adId)).thenReturn(commentEntities);
        when(commentMapper.toDto(commentEntity1)).thenReturn(comment1);
        when(commentMapper.toDto(commentEntity2)).thenReturn(comment2);

        // When
        Comments result = commentsService.getComments(adId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());
        verify(commentRepository, times(1)).findAllByAdIdOrderByCreatedAtDesc(adId);
    }

    @Test
    void addComment_ShouldSaveComment() {
        // Given
        Integer adId = 1;
        String username = "user@example.com";
        CreateOrUpdateComment commentDto = new CreateOrUpdateComment();
        commentDto.setText("Test comment");

        UserEntity author = new UserEntity();
        author.setId(1);

        AdEntity ad = new AdEntity();
        ad.setId(adId);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1);

        Comment expectedComment = new Comment();
        expectedComment.setPk(1);

        when(userService.getUserEntity(username)).thenReturn(author);
        when(adsService.getAdEntity(adId)).thenReturn(ad);
        when(commentMapper.toEntity(commentDto)).thenReturn(commentEntity);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toDto(commentEntity)).thenReturn(expectedComment);

        // When
        Comment result = commentsService.addComment(adId, commentDto, username);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPk());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
        assertNotNull(commentEntity.getCreatedAt());
    }

    @Test
    void deleteComment_ShouldDeleteComment_WhenUserIsOwner() {
        // Given
        Integer adId = 1;
        Integer commentId = 1;
        String username = "owner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setRole(ru.skypro.homework.dto.Role.USER);

        UserEntity commentAuthor = new UserEntity();
        commentAuthor.setId(1);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setAuthor(commentAuthor);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(commentRepository.findByIdAndAdId(commentId, adId)).thenReturn(Optional.of(commentEntity));

        // When
        commentsService.deleteComment(adId, commentId, username);

        // Then
        verify(commentRepository, times(1)).delete(commentEntity);
    }

    @Test
    void deleteComment_ShouldThrowSecurityException_WhenUserIsNotOwner() {
        // Given
        Integer adId = 1;
        Integer commentId = 1;
        String username = "notowner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2); // Different ID
        userEntity.setRole(ru.skypro.homework.dto.Role.USER);

        UserEntity commentAuthor = new UserEntity();
        commentAuthor.setId(1); // Different author

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setAuthor(commentAuthor);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(commentRepository.findByIdAndAdId(commentId, adId)).thenReturn(Optional.of(commentEntity));

        // When & Then
        assertThrows(SecurityException.class, () -> {
            commentsService.deleteComment(adId, commentId, username);
        });

        verify(commentRepository, never()).delete(any());
    }

    @Test
    void updateComment_ShouldUpdateComment_WhenUserIsOwner() {
        // Given
        Integer adId = 1;
        Integer commentId = 1;
        String username = "owner@example.com";
        CreateOrUpdateComment updateDto = new CreateOrUpdateComment();
        updateDto.setText("Updated comment");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        UserEntity commentAuthor = new UserEntity();
        commentAuthor.setId(1);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setAuthor(commentAuthor);

        Comment expectedComment = new Comment();
        expectedComment.setPk(commentId);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(commentRepository.findByIdAndAdId(commentId, adId)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);
        when(commentMapper.toDto(commentEntity)).thenReturn(expectedComment);

        // When
        Comment result = commentsService.updateComment(adId, commentId, updateDto, username);

        // Then
        assertNotNull(result);
        assertEquals(commentId, result.getPk());
        verify(commentMapper, times(1)).updateEntityFromDto(updateDto, commentEntity);
        verify(commentRepository, times(1)).save(commentEntity);
    }

    @Test
    void isCommentOwner_ShouldReturnTrue_WhenUserIsOwner() {
        // Given
        Integer commentId = 1;
        String username = "owner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        UserEntity commentAuthor = new UserEntity();
        commentAuthor.setId(1);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setAuthor(commentAuthor);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(userService.getUserEntity(username)).thenReturn(userEntity);

        // When
        boolean result = commentsService.isCommentOwner(commentId, username);

        // Then
        assertTrue(result);
    }

    @Test
    void isCommentOwner_ShouldReturnFalse_WhenUserIsNotOwner() {
        // Given
        Integer commentId = 1;
        String username = "notowner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2); // Different ID

        UserEntity commentAuthor = new UserEntity();
        commentAuthor.setId(1); // Different author

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentId);
        commentEntity.setAuthor(commentAuthor);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentEntity));
        when(userService.getUserEntity(username)).thenReturn(userEntity);

        // When
        boolean result = commentsService.isCommentOwner(commentId, username);

        // Then
        assertFalse(result);
    }
}