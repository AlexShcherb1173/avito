package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение выбрасывается, когда изображение с указанным ID не найдено в базе данных.
 * HTTP статус: 404 Not Found
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImageNotFoundException extends RuntimeException {
    
    public ImageNotFoundException(String message) {
        super(message);
    }
    
    public ImageNotFoundException(Integer id) {
        super("Изображение с id " + id + " не найдено");
    }
} 