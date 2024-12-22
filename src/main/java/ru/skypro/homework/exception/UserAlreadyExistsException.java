package ru.skypro.homework.exception;
/**
 * Класс обработки ошибки "UserAlreadyExistsException".
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists");
    }
}
