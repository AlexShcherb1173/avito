package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentsService {
    Comments getComments(Integer adId);
    Comment addComment(Integer adId, CreateOrUpdateComment comment, String username);

    @PreAuthorize("hasRole('ADMIN') or @commentsService.isCommentOwner(#commentId, authentication.name)")
    void deleteComment(Integer adId, Integer commentId, String username);

    @PreAuthorize("hasRole('ADMIN') or @commentsService.isCommentOwner(#commentId, authentication.name)")
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment, String username);

    boolean isCommentOwner(Integer commentId, String username);
}