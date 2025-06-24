package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение выбрасывается при неверном пароле при авторизации или смене пароля.
 * HTTP статус: 401 Unauthorized
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String message) {
        super(message);
    }
    
    public InvalidPasswordException() {
        super("Неверный пароль");
    }
} 