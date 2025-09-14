package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

/**
 * Сервис работы с профилем пользователя.
 * Отвечает за получение/обновление профиля и смену пароля.
 */

public interface UserService {

    /**
     * Меняет пароль авторизованного пользователя.
     *
     * @param username     логин (email) пользователя из Authentication
     * @param newPassword  DTO с текущим и новым паролем
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         если текущий пароль неверен
     */
    void updatePassword(String username, NewPassword newPassword);

    /**
     * Возвращает профиль текущего пользователя.
     *
     * @param username логин (email) пользователя
     * @return DTO профиля пользователя
     */

    User getUserInfo(String username);

    /**
     * Обновляет профиль текущего пользователя.
     *
     * @param username  логин (email) пользователя
     * @param updateUser DTO с полями для обновления (firstName/lastName/phone)
     * @return DTO с обновлёнными полями (по спецификации)
     */

    UpdateUser updateUser(String username, UpdateUser updateUser);

    /**
     * Обновляет аватар текущего пользователя.
     *
     * @param username логин (email) пользователя
     * @param image    файл изображения (multipart/form-data)
     */

    void updateUserImage(String username, MultipartFile image);
}
