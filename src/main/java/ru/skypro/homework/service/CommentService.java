package ru.skypro.homework.service;

import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getComments(Integer adId);

    CommentsDto createComment(Integer adId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username);

    void deleteComment(Integer adId, Integer commentId, String username);

}
