package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    boolean setPassword(String currentPassword, String newPassword);
    User getUserInfo();
    boolean updateUserInfo(UpdateUser user);
    boolean updateUserImage(String image);
}
