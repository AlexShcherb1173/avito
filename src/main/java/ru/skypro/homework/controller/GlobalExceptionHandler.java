package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IncorrectCurrentPasswordException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleForbiddenError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({FileProcessingException.class})
    public ResponseEntity<String> handleBadRequestError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({ImageNotFoundException.class, AdNotFoundException.class, CommentNotFoundException.class})
    public ResponseEntity<String> handleNotFoundError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
