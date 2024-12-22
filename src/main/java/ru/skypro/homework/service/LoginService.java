package ru.skypro.homework.service;

import ru.skypro.homework.dto.LoginDto;
/**
 * Интерефейс для сервиса авторизации.
 */
public interface LoginService {
    boolean login(LoginDto loginDto);

    boolean login(String username, String password);
}
