package ru.skypro.homework.service;

import ru.skypro.homework.dto.*;

public interface CommentService {
    /**
     * Возвращает комментарии к объявлению.
     * @param adId идентификатор объявления
     * @return DTO-обёртка со списком комментариев
     */
    Comments getComments(int adId);

    /**
     * Добавляет комментарий к объявлению от имени текущего пользователя.
     * @param adId идентификатор объявления
     * @param dto  текст комментария
     * @return созданный комментарий
     */
    Comment addComment(int adId, CreateOrUpdateComment dto);

    /**
     * Удаляет комментарий. Разрешено автору комментария или администратору.
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     */
    void deleteComment(int adId, int commentId);

    /**
     * Обновляет комментарий. Разрешено автору комментария или администратору.
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @param dto       новые данные (text)
     * @return обновлённый комментарий
     */
    Comment updateComment(int adId, int commentId, CreateOrUpdateComment dto);
}
