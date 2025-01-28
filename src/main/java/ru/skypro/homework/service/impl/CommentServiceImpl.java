package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentsDto getCommentsForAd(Integer adPk, Authentication authentication) {
        List<Comment> comments = commentRepository.findByAdPk(adPk);
        List<CommentDto> commentDtos = comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
        return new CommentsDto(commentDtos.size(), commentDtos);
    }

    @Override
    public CommentDto addComment(Integer adPk, CreateOrUpdateCommentDto commentDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(Instant.now().toEpochMilli());
        comment.setUser(user);
        // Здесь нужно добавить логику для установки объявления (Ad) по adId
        // Например, через AdRepository
        // comment.setAd(adRepository.findById(adId).orElseThrow(() -> new RuntimeException("Ad not found")));
        Comment savedComment = commentRepository.save(comment);
        return toCommentDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Integer adPk, Integer commentId, CreateOrUpdateCommentDto commentDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUser().getId().equals(user.getId())) {
                comment.setText(commentDto.getText());
                Comment updatedComment = commentRepository.save(comment);
                return toCommentDto(updatedComment);
            } else {
                throw new RuntimeException("User is not authorized to update this comment");
            }
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    @Override
    public void deleteComment(Integer adPk, Integer commentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUser().getId().equals(user.getId())) {
                commentRepository.delete(comment);
            } else {
                throw new RuntimeException("User is not authorized to delete this comment");
            }
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    private CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setAuthor(comment.getUser().getId());
        commentDto.setAuthorFirstName(comment.getUser().getFirstName());
        // Установите authorImage, если есть соответствующее поле в User
        // commentDto.setAuthorImage(comment.getUser().getImage());
        return commentDto;
    }
}