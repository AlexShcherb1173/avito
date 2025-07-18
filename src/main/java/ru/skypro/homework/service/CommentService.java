package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.List;

public interface CommentService {


    List<CommentDTO> getCommentsForAd(Integer adId);

    CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment);

    CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment);

    void deleteComment(Integer adId, Integer commentId);
}