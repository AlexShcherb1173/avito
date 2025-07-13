package ru.skypro.homework.service;

import ru.skypro.homework.dto.commentDTO.CommentCreateDTO;
import ru.skypro.homework.dto.commentDTO.CommentDTO;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CommentService {

    List<CommentDTO> getComments(Integer adId);

    void addComment(Integer id, CommentCreateDTO
            commentCreateDTO);

    void deleteComment(Integer adId, Integer commentId);

    void updateComment(Integer adId, Integer commentId);

}
