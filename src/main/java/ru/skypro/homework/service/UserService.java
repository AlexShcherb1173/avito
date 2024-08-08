package ru.skypro.homework.service;


import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

public interface UserService {

    UserDto getCurrentUser();

    UserDto updateUser(UpdateUserDto updateUserDto);

    Void setPassword(NewPasswordDto newPasswordDto);

    void updateUserImage(MultipartFile image, String email);
}
