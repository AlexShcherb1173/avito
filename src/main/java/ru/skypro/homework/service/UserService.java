package ru.skypro.homework.service;

import ru.skypro.homework.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long id);
}
