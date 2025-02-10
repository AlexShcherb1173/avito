package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;

import java.security.Principal;

public interface UserService {
    UserDTO getUser(Principal principal);

    void setPassword(NewPassword newPassword, Principal principal);

    UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO, Principal principal);

    void updateUserImage(MultipartFile image, Principal principal);
}
