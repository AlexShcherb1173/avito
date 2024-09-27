package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

import java.security.Principal;

public interface UserService {
    /**
     * Изменить пароль
     * @param newPasswordDto = старый пароль + новый пароль
     */
    void setPassword(NewPasswordDto newPasswordDto, Principal principal);

    /**
     * Получение информации об авторизованном пользователе
     * @param principal = интерфейс для получения username пользователя
     * @return = UserDto
     */
    UserDto getInfoAboutAuthUser(Principal principal);

    /**
     * Обновление информации об авторизованном пользователе
     * @param principal = интерфейс для получения username пользователя
     * @param updateUserDto
     * @return
     */
    UpdateUserDto setInfoAboutAuthUser(Principal principal, UpdateUserDto updateUserDto);

    /**
     * Обновление аватара авторизованного пользователя
     * @param principal = интерфейс для получения username пользователя
     * @param image = путь к файлу картинки
     */
    void setAvatar(MultipartFile image, Principal principal);

}
