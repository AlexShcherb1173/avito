package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CollectionMapper collectionMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UserEntity testUser;
    private AdEntity testAd;
    private CommentEntity testComment;
    private CommentDto testCommentDto;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Иван");

        testAd = new AdEntity();
        testAd.setId(1);
        testAd.setTitle("Test Ad");

        testComment = new CommentEntity();
        testComment.setId(1);
        testComment.setText("Test comment text");
        testComment.setAuthor(testUser);
        testComment.setAd(testAd);
        testComment.setCreatedAt(LocalDateTime.now());

        testCommentDto = new CommentDto();
        testCommentDto.setPk(1);
        testCommentDto.setText("Test comment text");
        testCommentDto.setAuthor(1);
        testCommentDto.setAuthorFirstName("Иван");
    }

    @Test
    void getComments_WhenAdExists_ShouldReturnCommentsDto() {
        // Given
        Integer adId = 1;
        List<CommentEntity> comments = List.of(testComment);
        CommentsDto expectedDto = new CommentsDto();
        expectedDto.setCount(1);
        expectedDto.setResults(List.of(testCommentDto));

        when(adRepository.existsById(adId)).thenReturn(true);
        when(commentRepository.findByAdId(adId)).thenReturn(comments);
        when(collectionMapper.toCommentsDto(comments)).thenReturn(expectedDto);

        // When
        CommentsDto result = commentService.getComments(adId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(adRepository).existsById(adId);
        verify(commentRepository).findByAdId(adId);
    }

    @Test
    void getComments_WhenAdNotExists_ShouldThrowException() {
        // Given
        Integer adId = 999;
        when(adRepository.existsById(adId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () ->
                commentService.getComments(adId));
        verify(adRepository).existsById(adId);
    }

    @Test
    void createComment_ShouldCreateAndReturnComment() {
        // Given
        Integer adId = 1;
        String username = "test@example.com";
        CreateOrUpdateCommentDto createDto = new CreateOrUpdateCommentDto();
        createDto.setText("New comment");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(testUser));
        when(adRepository.findById(adId)).thenReturn(Optional.of(testAd));
        when(commentMapper.toEntity(createDto)).thenReturn(testComment);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(testComment);
        when(commentMapper.toDto(testComment)).thenReturn(testCommentDto);

        // When
        CommentDto result = commentService.createComment(adId, createDto, username);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getPk());
        assertEquals("Test comment text", result.getText());
        verify(commentRepository).save(any(CommentEntity.class));
    }

    @Test
    void isCommentAuthor_WhenUserIsAuthor_ShouldReturnTrue() {
        // Given
        Integer commentId = 1;
        String username = "test@example.com";

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(testUser));

        // When
        boolean result = commentService.isCommentAuthor(commentId, username);

        // Then
        assertTrue(result);
    }

    @Test
    void isCommentAuthor_WhenUserIsNotAuthor_ShouldReturnFalse() {
        // Given
        Integer commentId = 1;
        String username = "other@example.com";

        UserEntity otherUser = new UserEntity();
        otherUser.setId(2);
        otherUser.setEmail("other@example.com");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(otherUser));

        // When
        boolean result = commentService.isCommentAuthor(commentId, username);

        // Then
        assertFalse(result);
    }
}