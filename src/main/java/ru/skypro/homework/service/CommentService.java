package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {
    CommentsDto commentsAd(int id);

    CommentDto createComment(int id, CreateOrUpdateCommentDto dto, UserDetails userDetails);

    void deleteComment(int id, int commentId);

    CommentDto updateComment(int id, int commentId, CreateOrUpdateCommentDto dto);

}
