package ru.skypro.homework.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

// Глобальный обработчик исключений для всего приложения.
// Обеспечивает единообразную обработку ошибок и возврат соответствующих HTTP-статусов.

@ControllerAdvice
public class GlobalExceptionHandler {

// Обработка исключений при превышении максимального размера файла.
// Возвращает HTTP 400 Bad Request с сообщением об ошибке.

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("File size exceeds the allowed limit. Maximum size is 10MB.");
    }

// Обработка ошибок валидации DTO объектов.
// Возвращает HTTP 400 Bad Request с детальной информацией о ошибках валидации.

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // Обработка исключений при попытке регистрации с существующим username.
    // Возвращает HTTP 409 Conflict с сообщением об ошибке.

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<String> handleUsernameExistsException(UsernameExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    // Обработка некорректных аргументов или бизнес-логики.
    // Возвращает HTTP 400 Bad Request с сообщением об ошибке.

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    // Обработка случаев, когда сущность не найдена в базе данных.
    // Возвращает HTTP 404 Not Found с сообщением об ошибке.

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // Обработка ошибок доступа (авторизации).
    // Возвращает HTTP 403 Forbidden с сообщением об ошибке.

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
    }

    // Обработка ошибок аутентификации.
    // Возвращает HTTP 401 Unauthorized с сообщением об ошибке.

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(
            org.springframework.security.core.AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed: " + e.getMessage());
    }

    // Универсальный обработчик всех непредвиденных исключений.
    // Возвращает HTTP 500 Internal Server Error с общим сообщением об ошибке.
    // В production следует логировать ошибку, но не возвращать детали клиенту.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        // В реальном приложении здесь следует залогировать полный stacktrace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }

    // Обработка исключений при работе с файлами (чтение/запись).
    // Возвращает HTTP 500 Internal Server Error с сообщением об ошибке.

    @ExceptionHandler(java.io.IOException.class)
    public ResponseEntity<String> handleIOException(java.io.IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File operation failed: " + e.getMessage());
    }

    // Обработка исключений при работе с некорректными данными.
    // Возвращает HTTP 422 Unprocessable Entity с сообщением об ошибке.

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Invalid request data: " + e.getMessage());
    }
}
