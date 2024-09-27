package ru.skypro.homework.exceptions;

public class PasswordsNotEqualsException  extends RuntimeException{

    @Override
    public String getMessage() {
        return "Пароль из базы данных не совпадает с текущем паролем из запроса";
    }
}