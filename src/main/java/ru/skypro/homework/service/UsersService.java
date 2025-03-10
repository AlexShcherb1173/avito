package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

public interface UsersService {
    public void setPassword(NewPasswordDto newPasswordDto);
    public UserDto getUser();
    public void updateUser(UpdateUserDTO updateUserDTO);
    public void uploadImage(MultipartFile file) throws IOException;

};
