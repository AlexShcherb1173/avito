package ru.skypro.homework.service;


import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments getForAd(Integer adId);
    CommentDto add(Integer adId, CreateOrUpdateComment dto);
    void delete(Integer adId, Integer commentId);
    CommentDto update(Integer adId, Integer commentId, CreateOrUpdateComment dto);
}