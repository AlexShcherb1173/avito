package ru.skypro.homework.exception;
/**
 * Класс обработки ошибки "UnauthorizedException".
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
