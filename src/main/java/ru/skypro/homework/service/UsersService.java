package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;

public interface UsersService {
    public String setPassword(NewPasswordDto newPasswordDto);
    public UserDto getUser();
    public String updateUser();

};
