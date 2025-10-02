package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    User getCurrentUser(Authentication authentication);

    void setPassword(Authentication authentication, String newPassword);

    UpdateUser updateUser(Authentication authentication, UpdateUser updateUser);
}