package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

public interface AuthService {
    /**
     * Метод для авторизации пользователя.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     */
    boolean authenticate(String username, String password);

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param register Форма регистрации пользователя
     */
    boolean register(Register register);
}