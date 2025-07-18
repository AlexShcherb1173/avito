package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CommentControllerTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentController commentController;

    @Test
    void addComment_Success() {
        // Arrange
        Users user = new Users();
        Ad ad = new Ad();
        CreateOrUpdateComment commentDto = new CreateOrUpdateComment();
        commentDto.setText("Test comment");

        when(authentication.getPrincipal()).thenReturn(user);
        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<CommentDTO> response = commentController.addComment(1, commentDto, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test comment", response.getBody().getText());
    }

    @Test
    void deleteComment_Success() {
        // Act
        ResponseEntity<Void> response = commentController.deleteComment(1, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository, times(1)).deleteByIdAndAdId(1, 1);
    }
}