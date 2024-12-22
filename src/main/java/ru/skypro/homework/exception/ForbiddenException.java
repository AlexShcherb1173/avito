package ru.skypro.homework.exception;
/**
 * Класс обработки ошибки "ForbiddenException".
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
