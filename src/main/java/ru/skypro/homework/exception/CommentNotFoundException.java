package ru.skypro.homework.exception;

import ru.skypro.homework.service.impl.LoggingMethodImpl;

public class CommentNotFoundException extends RuntimeException{
   

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }

    public CommentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CommentNotFoundException() {
    }
        
}