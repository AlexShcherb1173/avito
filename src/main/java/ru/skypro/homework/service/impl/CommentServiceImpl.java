package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public CommentDto addComment(Long adId, String username, CreateOrUpdateComment dto) {
        log.info("Добавление комментария к объявлению ID: {} от пользователя: {}", adId, username);

        // 1. Находим объявление
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> {
                    log.warn("Объявление не найдено: ID={}", adId);
                    return new EntityNotFoundException("Ad not found with id: " + adId);
                });

        // 2. Находим автора
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", username);
                    return new EntityNotFoundException("User not found with username: " + username);
                });

        // 3. Создаём комментарий
        Comment comment = new Comment();
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setText(dto.getText());
        comment.setCreatedAt(LocalDateTime.now());

        // 4. Сохраняем
        Comment saved = commentRepository.save(comment);

        // 5. Используем MapStruct для маппинга
        CommentDto result = CommentMapper.INSTANCE.toCommentDto(saved);

        log.info("Комментарий успешно добавлен, ID: {}", result.getPk());
        return result;
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        log.info("Удаление комментария ID: {} для объявления ID: {}", commentId, adId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Комментарий не найден: ID={}", commentId);
                    return new EntityNotFoundException("Comment not found with id: " + commentId);
                });

        // Проверяем, что комментарий принадлежит указанному объявлению
        if (!comment.getAd().getId().equals(adId)) {
            log.warn("Комментарий ID={} не принадлежит объявлению ID={}", commentId, adId);
            throw new IllegalArgumentException("Comment with id " + commentId + " does not belong to ad with id " + adId);
        }

        commentRepository.delete(comment);
        log.info("Комментарий успешно удален: ID={}", commentId);
    }

    @Override
    public CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateComment dto) {
        log.info("Обновление комментария ID: {} для объявления ID: {}", commentId, adId);
        log.debug("Новый текст: {}", dto.getText());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.warn("Комментарий не найден: ID={}", commentId);
                    return new EntityNotFoundException("Comment not found with id: " + commentId);
                });

        // Проверяем, что комментарий принадлежит указанному объявлению
        if (!comment.getAd().getId().equals(adId)) {
            log.warn("Комментарий ID={} не принадлежит объявлению ID={}", commentId, adId);
            throw new IllegalArgumentException("Comment with id " + commentId + " does not belong to ad with id " + adId);
        }

        comment.setText(dto.getText());
        Comment updated = commentRepository.save(comment);

        log.info("Комментарий успешно обновлен: ID={}", commentId);
        return CommentMapper.INSTANCE.toCommentDto(updated);
    }

    @Override
    public CommentsResponse getComments(Long adId) {
        log.info("Получение комментариев для объявления ID: {}", adId);

        List<Comment> comments = commentRepository.findByAdId(adId);
        List<CommentDto> dtos = CommentMapper.INSTANCE.toCommentDtoList(comments);

        log.debug("Найдено {} комментариев для объявления ID: {}", dtos.size(), adId);
        return new CommentsResponse(dtos.size(), dtos);
    }

    @Override
    public boolean isCommentAuthor(Long commentId, String username) {
        log.debug("Проверка авторства комментария ID: {} для пользователя: {}", commentId, username);

        return commentRepository.findById(commentId)
                .map(comment -> {
                    boolean isAuthor = comment.getAuthor().getUsername().equals(username);
                    log.debug("Пользователь {} {} автором комментария ID: {}",
                            username, isAuthor ? "является" : "не является", commentId);
                    return isAuthor;
                })
                .orElse(false);
    }
}