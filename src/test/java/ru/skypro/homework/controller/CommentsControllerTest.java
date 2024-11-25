package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentsControllerTest {

    @Mock
    private CommentsServiceImpl commentsService;

    @InjectMocks
    private CommentsController commentsController;

    @Test
    public void testGetCommentsByListing() {
        // Arrange
        long listingId = 1;
        CommentsDTO commentsDTO = new CommentsDTO();
        when(commentsService.getComments(listingId)).thenReturn(commentsDTO);

        // Act
        ResponseEntity<CommentsDTO> response = commentsController.getCommentsByListing(listingId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commentsDTO, response.getBody());
    }

    @Test
    public void testAddCommentFromListing() {
        // Arrange
        long listingId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        Authentication authentication = mock(Authentication.class);
        CommentDTO commentDTO = new CommentDTO();
        when(commentsService.addComment(listingId, createOrUpdateComment, authentication)).thenReturn(commentDTO);

        // Act
        ResponseEntity<CommentDTO> response = commentsController.addCommentFromListing(listingId, createOrUpdateComment, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commentDTO, response.getBody());
    }

    @Test
    public void testDeleteComment() throws AccessDeniedException {
        // Arrange
        long adId = 1;
        long commentId = 1;
        Authentication authentication = mock(Authentication.class);

        // Act
        ResponseEntity<?> responseEntity = commentsController.deleteComment(adId, commentId, authentication);

        // Assert
        verify(commentsService).deleteComment(adId, commentId, authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateComment() throws AccessDeniedException {
        // Arrange
        long adId = 1;
        long commentId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        Authentication authentication = mock(Authentication.class);
        CommentDTO commentDTO = new CommentDTO();
        when(commentsService.updateComment(adId, commentId, createOrUpdateComment, authentication)).thenReturn(commentDTO);

        // Act
        ResponseEntity<CommentDTO> responseEntity = commentsController.updateComment(adId, commentId, createOrUpdateComment, authentication);

        // Assert
        verify(commentsService).updateComment(adId, commentId, createOrUpdateComment, authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentDTO, responseEntity.getBody());
    }

}
