package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис авторизации и регистрации пользователей.
 *
 * Определяет операции входа в систему,
 * регистрации нового пользователя
 * и смены пароля.
 */
public interface AuthService {

    /**
     * Проверить логин и пароль пользователя.
     *
     * @param userName логин пользователя
     * @param password пароль пользователя
     * @return true, если логин и пароль корректны
     */
    boolean login(String userName, String password);

    /**
     * Зарегистрировать нового пользователя.
     *
     * @param register объект с данными регистрации
     * @return true, если пользователь успешно зарегистрирован
     */
    boolean register(Register register);

    /**
     * Сменить пароль пользователя.
     *
     * @param username логин пользователя
     * @param oldPassword текущий пароль
     * @param newPassword новый пароль
     * @return true, если пароль был успешно изменен
     */
    boolean changePassword(String username, String oldPassword, String newPassword);
}
