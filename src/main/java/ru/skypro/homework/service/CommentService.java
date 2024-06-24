package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;

public interface CommentService {
    CommentsDTO getComments(int adId);

    CommentDTO addComment(int adId, String name, CreateOrUpdateCommentDTO comment);

    CommentDTO updateComment(int adId, int commentId, CreateOrUpdateCommentDTO comment);

    void deleteComment(int adId, int commentId);
}
