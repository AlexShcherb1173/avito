package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.User;

public interface UserService {

    void updatePassword(User user, String newPassword);

    void updateUserInfo(User user, User updatedUserInfo);

    void updateUserImage(User user, MultipartFile image);
}
