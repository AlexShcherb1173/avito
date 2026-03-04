package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public Comments getComments(Long adId) {

        List<ru.skypro.homework.entity.Comment> comments =
                commentRepository.findByAdId(adId);

        List<Comment> dtoList =
                comments.stream()
                        .map(commentMapper::toDto)
                        .collect(Collectors.toList());

        Comments result = new Comments();
        result.setCount(dtoList.size());
        result.setResults(dtoList);

        return result;
    }

    @Override
    public Comment addComment(Long adId, CreateOrUpdateComment createComment) {

        ru.skypro.homework.entity.Ad ad =
                adRepository.findById(adId)
                        .orElseThrow(() -> new RuntimeException("Ad not found"));

        User author = getCurrentUser();

        ru.skypro.homework.entity.Comment comment =
                commentMapper.toEntity(createComment);

        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        ru.skypro.homework.entity.Comment saved =
                commentRepository.save(comment);

        return commentMapper.toDto(saved);
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {

        ru.skypro.homework.entity.Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(comment, currentUser)) {
            throw new AccessDeniedException("You cannot delete чужой комментарий");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Long adId, Long commentId, CreateOrUpdateComment updateComment) {

        ru.skypro.homework.entity.Comment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found"));

        User currentUser = getCurrentUser();

        if (!isOwnerOrAdmin(comment, currentUser)) {
            throw new AccessDeniedException("You cannot edit чужой комментарий");
        }

        comment.setText(updateComment.getText());

        ru.skypro.homework.entity.Comment updated =
                commentRepository.save(comment);

        return commentMapper.toDto(updated);
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isOwnerOrAdmin(ru.skypro.homework.entity.Comment comment, User user) {
        return comment.getAuthor().getId().equals(user.getId())
                || user.getRole().name().equals("ADMIN");
    }
}