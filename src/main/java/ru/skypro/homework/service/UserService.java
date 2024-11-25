package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.io.IOException;

public interface UserService {

    void updatePassword(NewPassword newPassword, String username);

    User getUser(String username);

    void updateUser(UpdateUser updateUser, String username) throws IOException;

    void updateUserAvatar(MultipartFile image, String username) throws IOException;

    byte[] findAvatarImageByFilename(String fileName) throws IOException;

    String getExtension(MultipartFile file);
}
