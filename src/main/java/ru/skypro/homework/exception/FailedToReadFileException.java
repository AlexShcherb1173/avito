package ru.skypro.homework.exception;

public class FailedToReadFileException extends RuntimeException {
    public FailedToReadFileException(String message) {
        super("Not found");
    }
}
