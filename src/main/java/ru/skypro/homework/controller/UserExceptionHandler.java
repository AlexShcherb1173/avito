package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@CrossOrigin("http://localhost:3000/")
public class UserExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(
                "Произошла ошибка: " + exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}