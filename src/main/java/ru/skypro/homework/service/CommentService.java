package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.Comment;

import java.util.List;

public interface CommentService {


    Comment addCommentToAds(int id, CreateOrUpdateComment text, Authentication authentication);

//    Comment addCommentToAds(Integer id, CreateOrUpdateComment text, Authentication authentication);

    boolean deleteComment(int adId, int commentId, Authentication authentication);



    Comment update(int adId, int commentId, CreateOrUpdateComment text, Authentication authentication);

    List<Comment> getComments(int id);
}
