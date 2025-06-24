package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение выбрасывается, когда объявление с указанным ID не найдено в базе данных.
 * HTTP статус: 404 Not Found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdNotFoundException extends RuntimeException {
    
    public AdNotFoundException(String message) {
        super(message);
    }
    
    public AdNotFoundException(Integer id) {
        super("Объявление с id " + id + " не найдено");
    }
} 