package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {

    Comments getComments(Long adId);

    Comment addComment(Long adId, CreateOrUpdateComment createComment);

    void deleteComment(Long adId, Long commentId);

    Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment updateComment);
}