package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UserUpdateInfoDTO;

import jakarta.transaction.Transactional;
import java.security.Principal;

public interface UserService {
    void setPassword(NewPassword newPassword, Principal principal);

    UserDTO showUserInfo(Principal principal);

    @Transactional
    void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO, Principal principal);

}