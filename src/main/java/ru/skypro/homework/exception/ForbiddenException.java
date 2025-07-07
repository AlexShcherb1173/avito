package ru.skypro.homework.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException() {
        super("Forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
