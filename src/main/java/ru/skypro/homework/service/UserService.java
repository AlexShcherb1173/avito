package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

@Service
public interface UserService {
    void setPassword(NewPasswordDto newPasswordDto);

    UserDto getCurrentUserInformation();

    UpdateUserDto updateUserInformation(UpdateUserDto updateUserDto);

    void uploadUserPicture(MultipartFile file) throws IOException;
}
