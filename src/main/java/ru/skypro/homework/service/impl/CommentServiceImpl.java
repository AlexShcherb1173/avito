package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.commentDTO.CommentCreateDTO;
import ru.skypro.homework.dto.commentDTO.CommentDTO;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public List<CommentDTO> getComments(Integer adId) {
        return null;
    }

    @Override
    public void addComment(Integer id, CommentCreateDTO
            commentCreateDTO) {
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {

    }

    @Override
    public void updateComment(Integer adId, Integer commentId) {

    }

}
