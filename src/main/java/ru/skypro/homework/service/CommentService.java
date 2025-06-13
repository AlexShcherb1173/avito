package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;

public interface CommentService {

    Comments getComments(Integer id);

    Long addComment(Integer id, Comment comment);

    void deleteComment(Integer adId, Integer commentId);

    Comment updateComment(Long id, Long commentId, Comment comment);

    Comments getCommentsByAdId(Long adId);
}
