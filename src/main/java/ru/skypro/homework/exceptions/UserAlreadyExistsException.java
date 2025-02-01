package ru.skypro.homework.exceptions;

import ru.skypro.homework.dto.Register;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Register register) {
        super("Пользователь с именем " + register.getUsername() + " уже существует");
    }
}