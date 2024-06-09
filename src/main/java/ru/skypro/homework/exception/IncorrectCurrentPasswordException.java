package ru.skypro.homework.exception;

public class IncorrectCurrentPasswordException extends RuntimeException {
    public IncorrectCurrentPasswordException(String message) {
        super(message);
    }
}
