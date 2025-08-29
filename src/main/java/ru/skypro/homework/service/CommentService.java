package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getComments(Integer id);

    CommentDto addComment(Integer id, CreateOrUpdateCommentDto createCommentDto);

    void deleteComment(Integer commentId);

    CommentDto updateComment(Integer commentId, CommentDto commentDto);

    void deleteCommentsByAdId(Integer adId);

    boolean hasCommentAccess(Integer CommentId);
}
