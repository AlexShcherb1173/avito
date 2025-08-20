package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;

import java.awt.*;

@Service
public class UserService {
    public boolean setPassword(NewPassword newPassword) {
        return false;
    }

    public UserDto getUser() {
        return null;
    }

    public boolean updateUser(UserDto user) {
        return false;
    }

    public void updateUserImage(MultipartFile file) {

    }

    public Image getUserImage(Integer id) {
        return null;
    }
}
