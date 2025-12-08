package ru.skypro.homework.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Перехватывает исключения и преобразует их в соответствующие HTTP ответы.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения {@link EntityNotFoundException}.
     * Возвращает HTTP статус 404 Not Found.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link SecurityException}.
     * Возвращает HTTP статус 403 Forbidden.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        log.warn("Security violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link IllegalArgumentException}.
     * Возвращает HTTP статус 400 Bad Request.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Обрабатывает исключения {@link MethodArgumentNotValidException}.
     * Возвращает HTTP статус 400 Bad Request с детализацией ошибок валидации.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + errorMessage);
    }

    /**
     * Обрабатывает исключения {@link ConstraintViolationException}.
     * Возвращает HTTP статус 400 Bad Request.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Constraint violation: " + ex.getMessage());
    }

    /**
     * Обрабатывает неспецифичные исключения {@link RuntimeException}.
     * Возвращает HTTP статус 500 Internal Server Error.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с сообщением об ошибке
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage());
    }

    /**
     * Обрабатывает все остальные исключения {@link Exception}.
     * Возвращает HTTP статус 500 Internal Server Error.
     *
     * @param ex перехваченное исключение
     * @return ResponseEntity с общим сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}