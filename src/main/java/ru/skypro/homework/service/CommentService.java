package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.repository.CommentRepository;

import java.util.Collection;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment editComment(Comment comment) {
        return commentRepository.save(comment);
    }
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
    public Collection<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    public Comment findCommentById(long id) {
        return commentRepository.findByCommentId(id);
    }
    public Comment updateComment (Comment comment) {
        return commentRepository.save(comment);
    }

}