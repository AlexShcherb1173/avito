package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import java.io.IOException;

public interface UserService {
    User getCurrentUserInfo();
    User updateUserInfo(UpdateUser updateUser); // Возвращает User вместо UpdateUser
    User setUserImage(MultipartFile imageFile) throws IOException; // Возвращает User вместо void
    boolean registerUser(ru.skypro.homework.dto.Register registerForm);
    boolean setPassword(ru.skypro.homework.dto.NewPassword newPassword,
                        org.springframework.security.core.Authentication authentication);
}