package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterDto;

public interface AuthService {

    /**
     * Авторизация пользователя.
     *
     * @param userName Логин пользователя.
     * @param password Пароль пользователя.
     * @return true, если авторизация успешна, иначе false.
     */
    boolean login(String userName, String password);

    /**
     * Регистрация нового пользователя.
     *
     * @param registerDto Данные для регистрации.
     * @return true, если регистрация успешна, иначе false.
     */
    boolean register(RegisterDto registerDto);
}