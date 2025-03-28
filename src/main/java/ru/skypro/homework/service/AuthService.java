package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;

public interface AuthService {

    void register(RegisterDto registerDto);

    void login(LoginDto loginDto);
}
