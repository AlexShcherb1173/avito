package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.authentication.RegisterDto;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserNotFoundException;

public interface UserService {
    UserDto getUserDto(String userName);

    User getUser(String username) throws UserNotFoundException;

    RegisterDto registerUser(RegisterDto body);

    byte[] getUserImage(int id) throws UserNotFoundException;

    UpdateUserDto updateUser(UpdateUserDto body, Authentication authentication) throws UserNotFoundException;

    void updateNewPassword(NewPasswordDto body, Authentication authentication) throws UserNotFoundException;

    void uploadImage(MultipartFile image, Authentication authentication) throws UserNotFoundException;
}
