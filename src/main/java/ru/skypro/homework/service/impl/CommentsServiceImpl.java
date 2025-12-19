package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentsService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final AdsService adsService;

    @Override
    @Transactional(readOnly = true)
    public Comments getComments(Integer adId) {
        log.info("Getting comments for ad with id: {}", adId);
        List<CommentEntity> commentEntities = commentRepository.findAllByAdIdOrderByCreatedAtDesc(adId);

        List<Comment> comments = commentEntities.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        Comments result = new Comments();
        result.setCount(comments.size());
        result.setResults(comments);
        return result;
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment comment, String username) {
        log.info("Adding comment to ad with id: {} by user: {}", adId, username);

        UserEntity author = userService.getUserEntity(username);
        AdEntity ad = adsService.getAdEntity(adId);

        CommentEntity commentEntity = commentMapper.toEntity(comment);
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(Instant.now());

        CommentEntity savedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, String username) {
        log.info("Deleting comment with id: {} from ad with id: {} by user: {}", commentId, adId, username);

        UserEntity user = userService.getUserEntity(username);
        CommentEntity commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!user.getRole().equals(ru.skypro.homework.dto.Role.ADMIN) &&
            !commentEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to delete this comment");
        }

        commentRepository.delete(commentEntity);
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment, String username) {
        log.info("Updating comment with id: {} for ad with id: {} by user: {}", commentId, adId, username);

        UserEntity user = userService.getUserEntity(username);
        CommentEntity commentEntity = commentRepository.findByIdAndAdId(commentId, adId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!commentEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to update this comment");
        }

        commentMapper.updateEntityFromDto(comment, commentEntity);
        CommentEntity updatedComment = commentRepository.save(commentEntity);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCommentOwner(Integer commentId, String username) {
        try {
            CommentEntity commentEntity = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
            UserEntity userEntity = userService.getUserEntity(username);
            return commentEntity.getAuthor().getId().equals(userEntity.getId());
        } catch (Exception e) {
            log.warn("Error checking comment ownership: {}", e.getMessage());
            return false;
        }
    }
}