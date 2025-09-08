package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

/**
 * Реализация {@link UserService}.
 * Отвечает за работу с профилем пользователя: получение и обновление информации, смену пароля, обновление аватара.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Меняет пароль пользователя.
     *
     * @param username    логин пользователя (email)
     * @param newPassword DTO с текущим и новым паролем
     */
    @Override
    public void updatePassword(String username, NewPassword newPassword) {
        // TODO: Реализовать смену пароля
    }

    /**
     * Получает профиль пользователя по логину.
     *
     * @param username логин пользователя (email)
     * @return объект {@link User} с данными профиля
     */
    @Override
    public User getUserInfo(String username) {
        // TODO: Получить инфу о пользователе
        return new User();
    }

    /**
     * Обновляет профиль пользователя (имя, фамилию, телефон).
     *
     * @param username    логин пользователя (email)
     * @param updateUser  объект с новыми значениями
     * @return обновлённый профиль
     */
    @Override
    public UpdateUser updateUser(String username, UpdateUser updateUser) {
        // TODO: Обновить юзера
        return updateUser;
    }

    /**
     * Обновляет изображение профиля (аватар) пользователя.
     *
     * @param username логин пользователя (email)
     * @param image    новый аватар (файл)
     */
    @Override
    public void updateUserImage(String username, MultipartFile image) {
        // TODO: Загрузить аватар
    }
}
