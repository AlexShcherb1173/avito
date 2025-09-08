package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;

/**
 * Сервис аутентификации и управления паролем.
 * Отвечает за регистрацию, логин и смену пароля пользователей.
 */
public interface AuthService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param dto DTO с данными регистрации (логин, пароль, роль)
     */
    void register(Register dto);

    /**
     * Проводит аутентификацию пользователя.
     *
     * @param dto DTO с логином и паролем
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         если логин или пароль неверны
     */
    void login(Login dto);

    /**
     * Меняет пароль текущего пользователя.
     *
     * @param username логин (email) пользователя, полученный из SecurityContext
     * @param dto DTO с текущим и новым паролем
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         если текущий пароль указан неверно
     */
    void changePassword(String username, NewPassword dto);
}
