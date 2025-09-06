package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public void updatePassword(String username, NewPassword newPassword) {
        // TODO: Реализовать смену пароля
    }

    @Override
    public User getUserInfo(String username) {
        // TODO: Получить инфу о пользователе
        return new User();
    }

    @Override
    public UpdateUser updateUser(String username, UpdateUser updateUser) {
        // TODO: Обновить юзера
        return updateUser;
    }

    @Override
    public void updateUserImage(String username, MultipartFile image) {
        // TODO: Загрузить аватар
    }
}
