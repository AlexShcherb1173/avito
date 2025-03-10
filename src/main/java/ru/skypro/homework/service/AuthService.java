package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterDTO;

/**
 * Сервисный интерфейс для аутентификации и регистрации пользователей.
 *
 * Интерфейс предоставляет методы для выполнения операций аутентификации и регистрации:
 * - Вход в систему с использованием логина и пароля {@link #login(String, String)}.
 * - Регистрация нового пользователя с использованием данных из {@link RegisterDTO} {@link #register(RegisterDTO)}.
 *
 * Методы интерфейса возвращают булевы значения, указывающие на успешность выполнения операции.
 */
public interface AuthService {

    boolean login(String userName, String password);

    boolean register(RegisterDTO register);
}
