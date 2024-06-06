package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

public interface UserService {
    boolean setPassword(NewPasswordDto newPasswordDto);

    UserDto getUser();

    UpdateUserDto updateUser(UpdateUserDto updateUserDto);

     void updateUserImage(MultipartFile file) throws IOException;

    byte[] getImage();
}
