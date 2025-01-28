package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {
    CommentsDto getCommentsForAd(Integer adPk, Authentication authentication);
    CommentDto addComment(Integer adPk, CreateOrUpdateCommentDto comment, Authentication authentication);
    CommentDto updateComment(Integer adPk, Integer commentId, CreateOrUpdateCommentDto comment, Authentication authentication);
    void deleteComment(Integer adPk, Integer commentId, Authentication authentication);
}