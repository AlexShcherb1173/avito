package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserHasNoRightsException extends RuntimeException {
    public UserHasNoRightsException(String message) {
        super(message);
    }
}
