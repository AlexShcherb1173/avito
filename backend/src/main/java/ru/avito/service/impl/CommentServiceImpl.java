package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.avito.dto.comment.CommentDto;
import ru.avito.dto.comment.CommentsResponse;
import ru.avito.dto.comment.CreateOrUpdateCommentRequest;
import ru.avito.entity.Ad;
import ru.avito.entity.Comment;
import ru.avito.entity.User;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.CommentMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.CommentRepository;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.AccessService;
import ru.avito.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AccessService accessService;

    @Override
    public CommentsResponse getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new NotFoundException("Ad not found");
        }

        List<CommentDto> results = commentRepository.findAllByAdIdOrderByCreatedAtAsc(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        return new CommentsResponse(results.size(), results);
    }

    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentRequest request) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad not found"));

        User currentUser = getCurrentUser();

        Comment comment = Comment.builder()
                .text(request.getText())
                .createdAt(Instant.now())
                .author(currentUser)
                .ad(ad)
                .build();

        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentRequest request) {
        Comment comment = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        User currentUser = getCurrentUser();
        accessService.checkCommentEditAccess(currentUser, comment);

        comment.setText(request.getText());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toDto(saved);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        Comment comment = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        User currentUser = getCurrentUser();
        accessService.checkCommentDeleteAccess(currentUser, comment);

        commentRepository.delete(comment);
    }

    private User getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
    }
}