package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {
    CommentsDto getCommentsForAd(Integer adPk);
    CommentDto addComment(Integer adPk, CreateOrUpdateCommentDto comment);
    CommentDto updateComment(Integer adPk, Integer commentId, CreateOrUpdateCommentDto comment);
    void deleteComment(Integer adPk, Integer commentId);
}