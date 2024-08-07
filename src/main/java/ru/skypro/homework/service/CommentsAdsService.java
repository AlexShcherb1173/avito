package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;

public interface CommentsAdsService {
    CommentDto createComment(CommentDto commentDto);

    CommentDto getCommentById(Long id);
}
