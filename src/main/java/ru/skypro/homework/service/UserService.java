package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    void updatePassword(String username, NewPassword newPassword);

    User getUserInfo(String username);

    UpdateUser updateUser(String username, UpdateUser updateUser);

    void updateUserImage(String username, MultipartFile image);
}
