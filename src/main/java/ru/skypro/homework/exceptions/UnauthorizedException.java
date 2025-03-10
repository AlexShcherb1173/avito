package ru.skypro.homework.exceptions;

import ru.skypro.homework.dto.Login;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(Login login) {
        super(String.format("Пользователь %s не авторизован", login.getUsername()));
    }
}