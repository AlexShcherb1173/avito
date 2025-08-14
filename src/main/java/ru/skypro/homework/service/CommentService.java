package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.dto.comment.Comments;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsForAd(Integer adId);
    CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment, String username);
    CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment, Authentication authentication);
    void deleteComment(Integer adId, Integer commentId, Authentication authentication);
}