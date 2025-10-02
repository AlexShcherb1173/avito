package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {
    UserDto getCurrentUser(Authentication authentication);

    void setPassword(Authentication authentication, String newPassword);

    UpdateUserDto updateUser(Authentication authentication, UpdateUserDto updateUser);
}