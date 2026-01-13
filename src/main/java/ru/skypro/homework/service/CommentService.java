package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdService adService;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, UserService userService,
                          AdService adService, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.adService = adService;
        this.commentMapper = commentMapper;
    }

    public Comments getComments(Integer adId) {
        log.info("Получение комментариев для объявления ID: {}", adId);
        List<CommentEntity> comments = commentRepository.findByAd_Pk(adId);
        Comments result = new Comments();
        result.setCount(comments.size());
        result.setResults(commentMapper.toDtoList(comments));
        return result;
    }

    @Transactional
    public Comment addComment(Integer adId, CreateOrUpdateComment commentDto, Authentication authentication) {
        log.info("Добавление комментария к объявлению ID: {}", adId);

        AdEntity ad = adService.getAdEntity(adId);
        UserEntity author = userService.getUserEntity(authentication.getName());

        CommentEntity comment = CommentEntity.builder()
                .ad(ad)
                .author(author)
                .text(commentDto.getText())
                .createdAt(LocalDateTime.now())
                .build();

        CommentEntity savedComment = commentRepository.save(comment);
        log.info("Добавлен комментарий ID: {}", savedComment.getPk());
        return commentMapper.toDto(savedComment);
    }

    @Transactional
    @PreAuthorize("@commentService.isCommentOwner(#commentId, authentication) or hasRole('ADMIN')")
    public void deleteComment(Integer adId, Integer commentId) {
        log.info("Удаление комментария ID: {} из объявления ID: {}", commentId, adId);

        CommentEntity comment = commentRepository.findById(commentId)
                .filter(c -> c.getAd().getPk().equals(adId))
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        commentRepository.delete(comment);
        log.info("Комментарий ID: {} удален", commentId);
    }

    @Transactional
    @PreAuthorize("@commentService.isCommentOwner(#commentId, authentication) or hasRole('ADMIN')")
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment) {
        log.info("Обновление комментария ID: {}", commentId);

        CommentEntity comment = commentRepository.findById(commentId)
                .filter(c -> c.getAd().getPk().equals(adId))
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
        }

        CommentEntity savedComment = commentRepository.save(comment);
        log.info("Комментарий ID: {} обновлен", commentId);
        return commentMapper.toDto(savedComment);
    }

    public boolean isCommentOwner(Integer commentId, Authentication authentication) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getUsername().equals(authentication.getName()))
                .orElse(false);
    }
}