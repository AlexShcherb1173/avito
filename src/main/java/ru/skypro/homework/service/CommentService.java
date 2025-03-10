package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final List<Comment> comments = new ArrayList<>();
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentsDTO getComments(int adId) {
        List<Comment> comments = commentRepository.findByAdId(adId);
        CommentsDTO response = new CommentsDTO();
        response.setCount(comments.size());
        response.setResults(comments);
        return response;
    }

    public Long addComment(int adId, CreateOrUpdateComment createComment) {
        if (createComment.getText() == null || createComment.getText().isEmpty()) {
            throw new IllegalArgumentException("Text of comment can't be empty.");
        }
        Comment newComment = new Comment();

        newComment.setAuthor(0); //заглушка
        newComment.setCreatedAt(System.currentTimeMillis());
        newComment.setText(createComment.getText());

        return commentRepository.save(newComment).getPk();
    }

    public void deleteComment(int adId, Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment updateComment(int adId, Long commentId, CreateOrUpdateComment updateComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        comment.setText(updateComment.getText());
        return commentRepository.save(comment);
    }
}

