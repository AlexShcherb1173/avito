package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

public interface UsersService {
    public String setPassword(NewPasswordDto newPasswordDto);
    public UserDto getUser();
    public String updateUser(UserDto userDto);
    public String uploadImage(MultipartFile file) throws IOException;

};
