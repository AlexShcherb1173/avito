package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;
@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentsDto getCommentsForAd(Integer id) {
        return null;
    }

    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto comment) {
        return null;
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto comment) {
        return null;
    }

    @Override
    public String deleteComment(Integer adId, Integer commentId) {
        return null;
    }
}
