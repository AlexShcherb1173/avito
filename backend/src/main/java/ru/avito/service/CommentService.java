package ru.avito.service;

import ru.avito.dto.comment.CommentDto;
import ru.avito.dto.comment.CommentsResponse;
import ru.avito.dto.comment.CreateOrUpdateCommentRequest;

public interface CommentService {

    CommentsResponse getComments(Integer adId);

    CommentDto addComment(Integer adId, CreateOrUpdateCommentRequest request);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentRequest request);

    void deleteComment(Integer adId, Integer commentId);
}