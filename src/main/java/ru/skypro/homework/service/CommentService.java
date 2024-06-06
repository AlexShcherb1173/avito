package ru.skypro.homework.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getComments(int id);

    CommentDto addComment(int id, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    CommentDto deleteComment(int adId, int commentId);

    CommentDto updateComment(int adId, int commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    boolean hasCommentAction(int adId, int commentId);
}
