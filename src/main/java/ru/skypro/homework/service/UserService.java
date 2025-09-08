package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

public interface UserService extends UserDetailsService {
    User getCurrentUser();
    User updateUser(UpdateUser updateUser);
    void updateUserImage(byte[] image, String filename);
    UserEntity getCurrentUserEntity();
}