package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, выбрасываемое когда комментарий не найден в базе данных
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {

    /**
     * Конструктор с ID комментария
     * @param id ID не найденного комментария
     */
    public CommentNotFoundException(Integer id) {
        super("Комментарий с ID " + id + " не найден");
    }

    /**
     * Конструктор с ID комментария и объявления
     * @param commentId ID комментария
     * @param adId ID объявления
     */
    public CommentNotFoundException(Integer commentId, Integer adId) {
        super("Комментарий с ID " + commentId + " для объявления с ID " + adId + " не найден");
    }
}