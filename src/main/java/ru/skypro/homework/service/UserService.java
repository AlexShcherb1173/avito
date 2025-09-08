package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

import java.io.IOException;

public interface UserService {
    User getCurrentUser(Authentication authentication);
    User updateUser(UpdateUser updateUser, Authentication authentication);
    void updateUserImage(MultipartFile image, Authentication authentication) throws IOException;
    UserEntity getCurrentUserEntity(Authentication authentication);
    byte[] getUserImage(String filename) throws IOException;
    boolean changePassword(String currentPassword, String newPassword, Authentication authentication);
}