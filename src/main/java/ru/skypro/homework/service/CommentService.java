package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {

    Comments getComments(Integer id);

    Long addComment(Integer id, Comment comment);

    Comment addComment(Integer id, CreateOrUpdateComment comment);

    void deleteComment(Integer adId, Integer commentId);

    Comment updateComment(Long id, Long commentId, Comment comment);

    Comment updateComment(Long adId, Integer commentId,
                          CreateOrUpdateComment createOrUpdateComment);

    Comments getCommentsByAdId(Long adId);

    Comment updateComment(Integer adId, Integer commentId,
                          CreateOrUpdateComment createOrUpdateComment);
}
