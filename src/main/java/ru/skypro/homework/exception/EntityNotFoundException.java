package ru.skypro.homework.exception;
/**
 * Класс обработки ошибки "EntityNotFoundException".
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
