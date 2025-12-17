package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.dto.comments.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional(readOnly = true)
    public CommentsDto getComments(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new EntityNotFoundException("Ad not found with id: " + adId);
        }
        List<CommentEntity> comments = commentRepository.findByAdId(adId);
        return collectionMapper.toCommentsDto(comments);
    }

    @Override
    public CommentDto createComment(Integer adId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username) {
        UserEntity author = getUserByUsername(username);
        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + adId));

        CommentEntity commentEntity = commentMapper.toEntity(createOrUpdateCommentDto);
        commentEntity.setAuthor(author);
        commentEntity.setAd(adEntity);

        CommentEntity savedComment = commentRepository.save(commentEntity);
        log.info("Created comment for ad: {} by user: {}", adId, username);

        return commentMapper.toDto(savedComment);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#commentId, #username)")
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto, String username) {
        CommentEntity commentEntity = getCommentById(commentId);

        // Проверяем, что комментарий принадлежит указанному объявлению
        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified ad");
        }

        commentMapper.updateEntityFromDto(commentEntity, createOrUpdateCommentDto);
        CommentEntity updatedComment = commentRepository.save(commentEntity);

        log.info("Updated comment: {} for ad: {} by user: {}", commentId, adId, username);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isCommentAuthor(#commentId, #username)")
    public void deleteComment(Integer adId, Integer commentId, String username) {
        CommentEntity commentEntity = getCommentById(commentId);

        // Проверяем, что комментарий принадлежит указанному объявлению
        if (!commentEntity.getAd().getId().equals(adId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified ad");
        }

        commentRepository.delete(commentEntity);
        log.info("Deleted comment: {} for ad: {} by user: {}", commentId, adId, username);
    }

    public boolean isCommentAuthor(Integer commentId, String username) {
        CommentEntity commentEntity = getCommentById(commentId);
        UserEntity userEntity = getUserByUsername(username);
        return commentEntity.getAuthor().getId().equals(userEntity.getId());
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    private CommentEntity getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
    }
}
