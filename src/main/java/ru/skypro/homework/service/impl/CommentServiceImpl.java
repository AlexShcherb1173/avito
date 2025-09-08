package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AdServiceImpl adService;
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    @Override
    public Comments getComments(Integer adId) {
        List<CommentEntity> commentEntities = commentRepository.findByAdId(adId);

        Comments comments = new Comments();
        comments.setCount(commentEntities.size());
        comments.setResults(commentEntities.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));
        return comments;
    }

    @Override
    @Transactional
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        AdEntity adEntity = adService.getAdEntity(adId);
        UserEntity author = userService.getCurrentUserEntity(authentication);

        CommentEntity commentEntity = commentMapper.toEntity(createOrUpdateComment);
        commentEntity.setAd(adEntity);
        commentEntity.setAuthor(author);
        commentEntity.setCreatedAt(Instant.now());

        CommentEntity savedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Verify the comment belongs to the specified ad
        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new RuntimeException("Comment does not belong to the specified ad");
        }

        // Check permissions
        if (!authService.isAdmin(authentication) &&
                !authService.isCurrentUser(authentication, commentEntity.getAuthor().getId())) {
            throw new RuntimeException("Access denied");
        }

        commentRepository.delete(commentEntity);
    }

    @Override
    @Transactional
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Verify the comment belongs to the specified ad
        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new RuntimeException("Comment does not belong to the specified ad");
        }

        // Check permissions
        if (!authService.isAdmin(authentication) &&
                !authService.isCurrentUser(authentication, commentEntity.getAuthor().getId())) {
            throw new RuntimeException("Access denied");
        }

        commentMapper.updateEntityFromDto(updateComment, commentEntity);
        CommentEntity updatedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(updatedComment);
    }
}
