package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;

public interface UserService {

    void updatePassword(NewPassword newPassword, String username);

    void getUser(UserEntity user);

    void updateUser(UpdateUser updateUser, String username);

    void updateUserImage(MultipartFile image, String username) throws IOException;

}
