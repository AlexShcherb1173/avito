package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

public interface CommentService {
    CommentDTO getCommentDTO (Comment comment);
    Comment createCommentFromDTO (CommentDTO commentDTO);

    CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO commentDTO);

    void deleteComment(Integer commentId);

    CommentsDTO getComments(Integer adId);

    CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO commentDTO);

    boolean isCommentAuthor(Integer commentId, String username);
}
