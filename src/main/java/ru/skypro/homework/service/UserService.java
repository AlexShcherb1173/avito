package ru.skypro.homework.service;

import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

public interface UserService {
    User getCurrentUser(String username);
    User updateUser(String username, UpdateUser updateUser);
    void updateUserImage(String username, String imagePath);
    void updatePassword(String username, String currentPassword, String newPassword);
    UserEntity getUserEntity(String username);
}