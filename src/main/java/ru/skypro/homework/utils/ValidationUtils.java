package ru.skypro.homework.utils;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtils {
    public static ResponseEntity<String> createErrorResponse(List<ObjectError> errors, HttpStatus status) {
        String errorMessage = errors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(status).body(errorMessage);
    }
}
