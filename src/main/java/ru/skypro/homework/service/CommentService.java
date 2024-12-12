package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;


public interface CommentService {

    public CommentsDto getCommentsForAd(Integer id);
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto comment);

    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto comment);


    public String deleteComment(Integer adId, Integer commentId);
}