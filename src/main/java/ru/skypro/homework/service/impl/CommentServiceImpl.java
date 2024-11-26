package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO add(CommentDTO comment) {

        CommentDTO commentDTO = commentRepository.save(comment);
        return commentDTO;
    }

    @Override
    public CommentDTO getComment(Long id) {
        return commentRepository.getById(id);
    }

    @Override public void delete(CommentDTO comment) {
        commentRepository.delete(comment);
    }

    @Override public CommentDTO update(CommentDTO comment) {
        return commentRepository.save(comment);
    }
}
