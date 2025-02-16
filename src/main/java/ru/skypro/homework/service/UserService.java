package ru.skypro.homework.service;

import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UserDTO;

import java.io.IOException;

public interface UserService extends UserDetailsManager {
    void createUser(RegisterDTO registerDTO);

    boolean setPassword(String userName, String password, String newPassword);

    UserDTO showUserInfo(LoginDTO loginDTO);

    void updateUserInfo(LoginDTO loginDTO, String firstName, String lastName, String phone);

    void uploadAvatar(LoginDTO loginDTO, MultipartFile multipartFile) throws IOException;

    void createAvatar(LoginDTO loginDTO, String filePath, MultipartFile multipartFile) throws IOException;
}
