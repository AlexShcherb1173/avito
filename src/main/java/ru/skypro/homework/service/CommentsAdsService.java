package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentsAdsService {


    CommentsDto getComments(Long id);

    CommentDto createComment(Long id, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    Void deleteComment(Long id, Long commentId);

    CommentDto updateComment(Long id, Long commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto);
}
