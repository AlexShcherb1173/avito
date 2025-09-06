package ru.skypro.homework.service;

import ru.skypro.homework.dto.*;

public interface CommentService {
    Comments getComments(int adId);
    Comment addComment(int adId, CreateOrUpdateComment dto);
    void deleteComment(int adId, int commentId);
    Comment updateComment(int adId, int commentId, CreateOrUpdateComment dto);
}
