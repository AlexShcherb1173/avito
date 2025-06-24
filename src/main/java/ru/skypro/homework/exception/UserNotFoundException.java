package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение выбрасывается, когда пользователь с указанным ID или логином не найден в базе данных.
 * HTTP статус: 404 Not Found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Integer id) {
        super("Пользователь с id " + id + " не найден");
    }
} 