package ru.skypro.homework.service;

import ru.skypro.homework.dto.authentication.RegisterDto;/*изначально эта зависимость была на файл в папке dto*/

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(RegisterDto registerDto);
}
