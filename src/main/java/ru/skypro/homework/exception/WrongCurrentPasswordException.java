package ru.skypro.homework.exception;

public class WrongCurrentPasswordException extends RuntimeException {

    public WrongCurrentPasswordException() {
        super("Неверный текущий пароль.");
    }

    public WrongCurrentPasswordException(String message) {
        super(message);
    }

    public WrongCurrentPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCurrentPasswordException(Throwable cause) {
        super(cause);
    }
}
