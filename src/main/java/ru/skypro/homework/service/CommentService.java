package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;

public interface CommentService {
    default Comments getCommentsByAdId(Long adId) {
        return null;
    }

    default Comment addComment(Long adId, Comment comment) {
        return null;
    }

    default void deleteComment(Long adId, Long commentId) {

    }

    Comment updateComment(Long adId, Long commentId, Comment comment);
}
