package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;


public interface CommentService {

    public Comments getCommentsForAd(Integer id);
    public Comment addComment(Integer adId, CreateOrUpdateComment comment);

    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment);


    public String deleteComment(Integer adId, Integer commentId);
}