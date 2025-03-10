package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDTO;

import java.io.IOException;

public interface UserService {
    void createUser(Register register);

    boolean setPassword(String userName, String password, String newPassword);

    UserDTO showUserInfo(Login login);

    void updateUserInfo(Login login, String firstName, String lastName, String phone);

    void uploadAvatar(Login login, MultipartFile multipartFile) throws IOException;

    void createAvatar(Login login, String filePath, MultipartFile multipartFile) throws IOException;
}
