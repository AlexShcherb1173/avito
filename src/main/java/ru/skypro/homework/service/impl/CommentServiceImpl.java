package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.CustomUserDetailsManager;

import java.util.List;
/**
 * Сервис для управления комментариями к объявлениям.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CustomUserDetailsManager customUserDetailsManager;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CustomUserDetailsManager customUserDetailsManager) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.customUserDetailsManager = customUserDetailsManager;
    }
    /**
     * Получает список комментариев для указанного объявления.
     */
    @Override
    public CommentsDto getCommentsForAd(Integer adId) {
        List<CommentDto> comments = commentRepository.findByAdId(adId).stream().map(CommentMapper::toDto).toList();
        return new CommentsDto(comments.size(), comments);
    }
    /**
     * Добавляет новый комментарий к объявлению.
     */
    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateCommentDto createCommentDto) {
        log.info("Пользователь {} добавил комментарий к объявлению {}",customUserDetailsManager.getCurrentUser().getId(),adId);
        Ad ad = adRepository.findById(adId).orElseThrow();
        Comment comment = CommentMapper.toComment(createCommentDto, customUserDetailsManager.getCurrentUser(), System.currentTimeMillis(), ad);
        return CommentMapper.toDto(commentRepository.save(comment));
    }
    /**
     * Обновляет комментарий к объявлению.
     */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto updateCommentDto) {
        log.info("Пользователь {} обновляет комментарий {}", customUserDetailsManager.getCurrentUser().getId(), commentId);
        Ad ad = adRepository.findById(adId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setText(updateCommentDto.getText());
        comment.setCreatedAt(System.currentTimeMillis());
        return CommentMapper.toDto(commentRepository.save(comment));
    }
    /**
     * Удаляет комментарий к объявлению.
     */
    @Override
    public void deleteComment(Integer commentId) {
        log.info("Пользователь {} удаляет комментарий {}", customUserDetailsManager.getCurrentUser().getId(), commentId);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Попытка удалить несуществующий комментарий "+commentId));
            commentRepository.delete(comment);
        } catch (EntityNotFoundException e) {
        }
    }
}
