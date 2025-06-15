package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {

    void setPassword(String username, NewPassword newPassword);

    User getUser(String username);

    UpdateUser updateUser(String username, UpdateUser updateUser);

    void updateUserImage(User user, MultipartFile image);

    void updatePassword(User user, String newPassword);

    void updateUserInfo(User user, User updatedUserInfo);
}
