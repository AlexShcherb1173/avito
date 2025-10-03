package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для работы с комментариями.
 * Включает логику добавления, редактирования и проверки прав доступа к комментариям.
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Проверка, может ли пользователь редактировать комментарий.
     * Пользователь может редактировать только свои комментарии, или если он администратор.
     *
     * @param commentId идентификатор комментария
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return true, если пользователь может редактировать комментарий, иначе false
     */
    public boolean canEditComment(Integer commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = userRepository.findByEmail(authentication.getName());
        return comment.getAuthor().getId().equals(user.getId()) || user.getRole().equals(User.Role.ADMIN);
    }

    /**
     * Добавляет новый комментарий.
     * Присваивает комментарий текущего авторизованного пользователя как автора.
     *
     * @param comment объект комментария
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return добавленный комментарий
     */
    public Comment addComment(Comment comment, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        comment.setAuthor(user);
        return commentRepository.save(comment);
    }
}
