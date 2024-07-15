package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getAllCommentsByAd(Integer adId);

    CommentDto addCommentToAd(Integer adId, CreateOrUpdateCommentDto commentDTO, Authentication authentication);

    void deleteComment(Integer adId, Integer commentId);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto commentDTO);

}
