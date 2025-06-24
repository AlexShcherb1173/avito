package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение выбрасывается, когда комментарий с указанным ID не найден в базе данных.
 * HTTP статус: 404 Not Found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {
    
    public CommentNotFoundException(String message) {
        super(message);
    }
    
    public CommentNotFoundException(Integer id) {
        super("Комментарий с id " + id + " не найден");
    }
} 