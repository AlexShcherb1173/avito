package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.generatedDto.CommentDto;
import ru.skypro.homework.dto.generatedDto.CommentsDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserHasNoRightsException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.CommentMapperImpl;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @Mock
    private AdService adService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Spy
    private CommentMapper commentMapper = new CommentMapperImpl();
    @InjectMocks
    private CommentServiceImpl out;

    private Ad testAd;
    private Comment testComment;
    private CommentDto testCommentDto;
    private User testUser;
    private Authentication auth;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");
        auth = new UsernamePasswordAuthenticationToken(testUser, null);

        testAd = new Ad();
        testAd.setId(1L);
        testAd.setPrice(new BigDecimal(10));
        testAd.setTitle("Test ads");

        testComment = new Comment();
        testComment.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1, 1));
        testComment.setText("Test comment");
        testComment.setId(1L);
        testComment.setAuthor(testUser);

        testAd.setComments(List.of(testComment));

        testCommentDto = commentMapper.commentToCommentDto(testComment);
    }

    @Test
    void shouldReturnResponseWrapperCommentWithAllCommentsForAd_whenGetAllCommentsForAdsWithId() {
        when(adService.getAdById(anyLong())).thenReturn(testAd);

        CommentsDto result = out.getAllCommentsForAd(testAd.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(testAd.getComments().size());
        assertThat(result.getResults().contains(commentMapper.commentToCommentDto(testComment))).isTrue();
    }

    @Test
    void shouldReturnCommentDto_WhenCreateNewComment() {
        when(adService.getAdById(anyLong())).thenReturn(testAd);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(userService.getUser(any(String.class))).thenReturn(null);

        CommentDto result = out.createNewComment(testAd.getId(), testCommentDto, auth);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(testCommentDto.getText());
        assertThat(result.getCreatedAt()).isEqualTo(testCommentDto.getCreatedAt());
        assertThat(result.getPk()).isEqualTo(testCommentDto.getPk());
    }

    @Test
    void shouldThrowUserHasNoRightsException_whenUserHasNORightsToDeleteComments() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        doThrow(UserHasNoRightsException.class).when(userService).checkUserHasPermit(auth, testUser.getUsername());

        assertThatExceptionOfType(UserHasNoRightsException.class)
                .isThrownBy(() -> out.deleteComment(testAd.getId(), testComment.getId(), auth));
    }

    @Test
    void shouldExecuteDeleteOnce_whenUserCanDeleteComments() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        doNothing().when(userService).checkUserHasPermit(auth, testUser.getUsername());

        out.deleteComment(testAd.getId(), testComment.getId(), auth);

        verify(commentRepository, atMostOnce()).delete(testComment);
    }

    @Test
    void shouldThrowUserHasNoRightsException_whenUserHasNORightsToUpdateComments() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        doThrow(UserHasNoRightsException.class).when(userService).checkUserHasPermit(auth, testUser.getUsername());

        assertThatExceptionOfType(UserHasNoRightsException.class)
                .isThrownBy(() -> out.updateComment(testAd.getId(),testComment.getId(), testCommentDto, auth));
    }

    @Test
    void shouldExecuteSaveOnce_whenUserCanUpdateAds() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(testComment));
        doNothing().when(userService).checkUserHasPermit(auth, testUser.getUsername());
        when(commentRepository.save(any())).thenReturn(testComment);

        CommentDto result = out.updateComment(testAd.getId(), testComment.getId(), testCommentDto, auth);

        verify(commentRepository, atMostOnce()).save(testComment);
        verify(commentMapper, atLeastOnce()).commentToCommentDto(testComment);

        assertThat(result).isNotNull();
    }
}