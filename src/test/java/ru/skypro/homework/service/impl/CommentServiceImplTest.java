package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.mapper.AdCommentsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdCommentsMapper adCommentsMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void getComments_returnsMappedDto() {
        long adId = 1L;
        Ad ad = new Ad();
        CommentsDto expected = new CommentsDto();
        when(adRepository.getReferenceById(adId)).thenReturn(ad);
        when(adCommentsMapper.toCommentsDto(ad)).thenReturn(expected);

        CommentsDto result = commentService.getComments(adId);

        assertSame(expected, result);
        verify(adRepository).getReferenceById(adId);
        verify(adCommentsMapper).toCommentsDto(ad);
        verifyNoMoreInteractions(adCommentsMapper, adRepository);
        verifyNoInteractions(commentRepository, commentMapper);
    }

    @Test
    void addComment_savesCommentAndReturnsDto() {
        long adId = 2L;
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Sample text");
        Ad ad = new Ad();
        Comment comment = new Comment();
        CommentDto expected = new CommentDto();

        when(adRepository.getReferenceById(adId)).thenReturn(ad);
        when(commentMapper.toComment(request, ad)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(expected);

        CommentDto result = commentService.addComment(adId, request);

        assertSame(expected, result);
        verify(adRepository).getReferenceById(adId);
        verify(commentMapper).toComment(request, ad);
        verify(commentRepository).save(comment);
        verify(commentMapper).toCommentDto(comment);
        verifyNoMoreInteractions(adRepository, commentRepository, commentMapper);
        verifyNoInteractions(adCommentsMapper);
    }

    @Test
    void deleteComment_delegatesToRepository() {
        long adId = 3L;
        long commentId = 4L;

        commentService.deleteComment(adId, commentId);

        verify(commentRepository).deleteById(commentId);
        verifyNoMoreInteractions(commentRepository);
        verifyNoInteractions(adRepository, commentMapper, adCommentsMapper);
    }

    @Test
    void updateComment_updatesTextAndReturnsDto() {
        long adId = 5L;
        long commentId = 6L;
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Updated text");
        Comment comment = new Comment();
        comment.setText("Old text");
        CommentDto expected = new CommentDto();

        when(commentRepository.getReferenceById(commentId)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(expected);

        CommentDto result = commentService.updateComment(adId, commentId, request);

        assertSame(expected, result);
        assertEquals("Updated text", comment.getText());
        verify(commentRepository).getReferenceById(commentId);
        verify(commentRepository).save(comment);
        verify(commentMapper).toCommentDto(comment);
        verifyNoMoreInteractions(commentRepository, commentMapper);
        verifyNoInteractions(adRepository, adCommentsMapper);
    }
}
