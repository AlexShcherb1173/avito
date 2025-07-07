package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.ForbiddenException;

import java.io.IOException;

public interface UsersService {
    // Обновление пароля
    void setPassword(NewPassword newPassword) throws ForbiddenException;

    // Получение информации об авторизованном пользователе
    User getUser();

    // Обновление информации об авторизованном пользователе
    UpdateUser updateUser(UpdateUser user);

    // Обновление аватара авторизованного пользователя
    void updateUserImage(MultipartFile image) throws IOException;
}
