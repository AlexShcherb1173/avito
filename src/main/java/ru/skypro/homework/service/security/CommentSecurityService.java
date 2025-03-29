package ru.skypro.homework.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

/**
 * Сервис безопасности для работы с комментариями.
 */
@Service
public class CommentSecurityService {
    private final CommentRepository commentRepository;

    /**
     * Конструктор CommentSecurityService.
     * @param commentRepository репозиторий комментариев
     */
    public CommentSecurityService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем комментария.
     * @param commentId идентификатор комментария
     * @return true, если пользователь является владельцем
     */
    public boolean isOwner(Integer commentId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        return comment.getAuthor().getEmail().equals(currentUsername);
    }
}