package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для обработки различных ошибок в приложении.
 *
 * Этот класс перехватывает исключения типа {@link RuntimeException} и возвращает соответствующие ответы.
 * В случае ошибки с сообщением "Access denied" возвращается статус HTTP 403 (FORBIDDEN).
 * Для других исключений типа {@link RuntimeException} возвращается статус HTTP 500 (INTERNAL_SERVER_ERROR).
 *
 * Методы этого класса используют аннотацию {@link ExceptionHandler} для перехвата исключений и возврата корректных HTTP-ответов.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAccessDenied(RuntimeException ex) {
        if (ex.getMessage().contains("Access denied")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
    }
}