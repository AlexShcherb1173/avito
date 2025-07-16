package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpadateComment;
import java.util.List;

public interface CommentService {

    List<CommentDTO> getComments(Integer adId);

    void addComment(Integer id, CreateOrUpadateComment
            createOrUpadateComment);

    void deleteComment(Integer adId, Integer commentId);

    void updateComment(Integer adId, Integer commentId);

}
