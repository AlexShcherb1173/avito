package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentsService {
    Comments getComments(Integer adId);
    Comment addComment(Integer adId, CreateOrUpdateComment comment, String username);
    void deleteComment(Integer adId, Integer commentId, String username);
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment, String username);
}