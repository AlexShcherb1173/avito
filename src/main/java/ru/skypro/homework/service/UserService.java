package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.UserEntity;

import java.io.IOException;

public interface UserService {
    UserEntity saveNewUser(UserEntity user);

    boolean setPassword(NewPassword newPassword);

    UserDto getUser();

    UserEntity getUser(String email);

    UpdateUser updateUser(UpdateUser updateUser);

    void updateUserImage(MultipartFile image) throws IOException;
}
