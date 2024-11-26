package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO add(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toDTO(savedComment);
    }

    @Override
    public CommentDTO getComment(Long id) {
        Comment comment = commentRepository.getById(id);
        return CommentMapper.INSTANCE.toDTO(comment);
    }

    @Override public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override public CommentDTO update(Comment comment) {
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toDTO(updatedComment);
    }
}
