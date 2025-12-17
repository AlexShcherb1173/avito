package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;

import java.io.IOException;

public interface UserService {
    UserDto getUser(String username);

    UpdateUserDto updateUser(UpdateUserDto updateUserDto, String username);

    boolean updatePassword(NewPasswordDto newPasswordDto, String username);

    boolean updateUserImage(MultipartFile image, String username) throws IOException;

    byte[] getUserImageById(Integer userId) throws IOException;

    String getUserImageContentTypeById(Integer userId) throws IOException;

    boolean deleteUserImage(String username) throws IOException;
}
