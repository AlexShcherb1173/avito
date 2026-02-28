package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис аутентификации и регистрации пользователей.
 * <p>
 * Используется контроллером {@link ru.skypro.homework.controller.AuthController}
 * для входа пользователя в систему и создания нового аккаунта.
 */

public interface AuthService {
    /**
     * Проверяет корректность логина и пароля пользователя.
     *
     * @param userName email пользователя
     * @param password пароль пользователя
     * @return {@code true}, если данные корректны, иначе {@code false}
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register DTO с данными для регистрации
     * @return {@code true}, если регистрация прошла успешно,
     *         {@code false}, если пользователь уже существует
     */
    boolean register(Register register);
}
