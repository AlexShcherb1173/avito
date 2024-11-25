package ru.skypro.homework.serviceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Listing;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ListingRepository;
import ru.skypro.homework.service.impl.CommentsServiceImpl;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceImplTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    @Test
    void testGetComments() {
        // Arrange
        long listingId = 1L;
        Comment comment = new Comment(1L, LocalDateTime.now(), "Test comment", new User(), new Listing());
        List<Comment> commentList = Collections.singletonList(comment);
        CommentDTO commentDTO = new CommentDTO(1, "image.png", "Test User", LocalDateTime.now(), 1, "Test comment");
        CommentsDTO expectedCommentsDTO = new CommentsDTO(1, Collections.singletonList(commentDTO));
        when(commentRepository.findCommentsByListingId(listingId)).thenReturn(commentList);
        when(commentMapper.commentToCommentDto(comment)).thenReturn(commentDTO);

        // Act
        CommentsDTO actualCommentsDTO = commentsService.getComments(listingId);

        // Assert
        assertEquals(expectedCommentsDTO, actualCommentsDTO);
        verify(commentRepository, times(1)).findCommentsByListingId(listingId);
        verify(commentMapper, times(1)).commentToCommentDto(comment);
    }

    @Test
    void testAddComment() {
        // Arrange
        long listingId = 1L;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("New comment");
        Listing listing = new Listing(1L, "Title", 100, "image.png");
        User user = new User(1L, "test@example.com", "password", "Test User");
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(new GetAuthentication().getAuthenticationUser(authentication.getName())).thenReturn(user);
        Comment comment = new Comment(1L, LocalDateTime.now(), createOrUpdateComment.getText(), user, listing);
        CommentDTO commentDTO = new CommentDTO(user.getId(), "image.png", "Test User", LocalDateTime.now(), listing.getId(), createOrUpdateComment.getText());
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(commentMapper.commentToCommentDto(comment)).thenReturn(commentDTO);

        // Act
        CommentDTO actualCommentDTO = commentsService.addComment(listingId, createOrUpdateComment, authentication);

        // Assert
        assertEquals(commentDTO, actualCommentDTO);
        verify(listingRepository, times(1)).findById(listingId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(commentMapper, times(1)).commentToCommentDto(comment);
    }

    @Test
    void testDeleteComment() {
        // Arrange
        long listingId = 1L;
        long commentId = 1L;
        Comment comment = new Comment(commentId, LocalDateTime.now(), "Test comment", new User(), new Listing());
        Authentication authentication = mock(Authentication.class);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> commentsService.deleteComment(listingId, commentId, authentication));
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void testUpdateComment() throws AccessDeniedException {
        // Arrange
        long listingId = 1L;
        long commentId = 1L;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("Updated comment");
        Comment comment = new Comment(commentId, LocalDateTime.now(), "Test comment", new User(), new Listing());
        Authentication authentication = mock(Authentication.class);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.commentToCommentDto(comment)).thenReturn(new CommentDTO(1, "image.png", "Test User", LocalDateTime.now(), 1, createOrUpdateComment.getText()));

        // Act
        CommentDTO actualCommentDTO = commentsService.updateComment(listingId, commentId, createOrUpdateComment, authentication);

        // Assert
        assertEquals(new CommentDTO(1, "image.png", "Test User", LocalDateTime.now(), 1, createOrUpdateComment.getText()), actualCommentDTO);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).commentToCommentDto(comment);
    }
}