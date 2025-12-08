package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Сервис для управления комментариями к объявлениям.
 * Предоставляет функционал для работы с комментариями,
 * включая проверку прав доступа для модификации.
 */
public interface CommentsService {

    /**
     * Получает все комментарии к указанному объявлению.
     * Комментарии возвращаются в порядке убывания даты создания.
     *
     * @param adId идентификатор объявления
     * @return объект {@link Comments} с количеством и списком комментариев
     */
    Comments getComments(Integer adId);

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId идентификатор объявления
     * @param comment данные комментария
     * @param username email автора комментария
     * @return созданный комментарий
     * @throws javax.persistence.EntityNotFoundException если объявление не найдено
     */
    Comment addComment(Integer adId, CreateOrUpdateComment comment, String username);

    /**
     * Удаляет комментарий.
     * Доступ разрешен только администраторам или авторам комментария.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param username email пользователя, выполняющего операцию
     * @throws javax.persistence.EntityNotFoundException если комментарий не найден
     * @throws SecurityException если пользователь не имеет прав на удаление
     */
    @PreAuthorize("hasRole('ADMIN') or @commentsServiceImpl.isCommentOwner(#commentId, authentication.name)")
    void deleteComment(Integer adId, Integer commentId, String username);

    /**
     * Обновляет текст комментария.
     * Доступ разрешен только администраторам или авторам комментария.
     *
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment обновленные данные комментария
     * @param username email пользователя, выполняющего операцию
     * @return обновленный комментарий
     * @throws javax.persistence.EntityNotFoundException если комментарий не найден
     * @throws SecurityException если пользователь не имеет прав на обновление
     */
    @PreAuthorize("hasRole('ADMIN') or @commentsServiceImpl.isCommentOwner(#commentId, authentication.name)")
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment comment, String username);

    /**
     * Проверяет, является ли пользователь автором комментария.
     *
     * @param commentId идентификатор комментария
     * @param username email пользователя
     * @return true если пользователь является автором, иначе false
     */
    boolean isCommentOwner(Integer commentId, String username);
}