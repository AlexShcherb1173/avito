package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.User;

import java.io.IOException;

public interface UserService {
    void setPassword(NewPasswordDto newPassword, Authentication authentication);
    User getAuthUserInfo(Authentication authentication);
    UpdateUserDto updateAuthUserInfo(UpdateUserDto updateUser, Authentication authentication);
    boolean updateAvatar(MultipartFile avatar, Authentication authentication) throws IOException;

}