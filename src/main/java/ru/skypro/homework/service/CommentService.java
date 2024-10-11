package ru.skypro.homework.service;

import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    boolean getCommentsOfAd(int id);
    boolean createComment(int id, CreateOrUpdateComment comment);
    boolean deleteComment(int adId, int commentId);
    boolean updateComment(int adId, int commentId, CreateOrUpdateComment comment);
}
