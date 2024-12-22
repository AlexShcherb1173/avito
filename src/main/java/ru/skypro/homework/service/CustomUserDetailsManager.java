package ru.skypro.homework.service;

import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.model.User;

import java.io.IOException;
/**
 * Интерефейс для сервиса пользователя.
 */
public interface CustomUserDetailsManager extends UserDetailsManager {
    void updateUser(User user);

    void changePassword(NewPasswordDto newPasswordDto);

    UpdateUserDto updateUser(UpdateUserDto updateUserDto);

    User getCurrentUser();

    void updateUserAvatar(MultipartFile image) throws IOException;

    void deleteExistingAvatar(User user) throws IOException;

    byte[] getAvatarFromFs(String avatarId) throws IOException;

    void createUser(RegisterDto registerDto);
}
