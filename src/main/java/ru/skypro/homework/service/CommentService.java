package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
/**
 * Интерефейс для сервиса комментариев.
 */
public interface CommentService {
    CommentsDto getCommentsForAd(Integer adId);

    CommentDto addComment(Integer adId, CreateOrUpdateCommentDto createCommentDto);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    void deleteComment(Integer commentId);
}
