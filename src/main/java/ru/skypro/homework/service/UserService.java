package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.register.NewPassword;
import ru.skypro.homework.dto.register.UpdateUser;
import ru.skypro.homework.dto.user.User;

public interface UserService {

    /**
     * Обновляет пароль пользователя.
     *
     * @param newPassword - данные для обновления пароля
     */
    void updatePassword(NewPassword newPassword);

    /**
     * Получает информацию о текущем пользователе.
     *
     * @return информация о пользователе
     */
    User getInfoAboutUser();

    /**
     * Обновляет информацию о пользователе.
     *
     * @param newInfoUser - новые данные пользователя
     * @return обновленная информация
     */
    UpdateUser updateInfoAboutUser(UpdateUser newInfoUser);

    /**
     * Обновляет аватар пользователя.
     *
     * @param image - файл изображения
     */
    void updateAvatarUser(MultipartFile image);

    /**
     * Получает аватар пользователя.
     *
     * @return изображение аватара
     */
    byte[] getUserAvatar();
}
