package ru.skypro.homework.service;


import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;

@Service
public interface AuthService {
    void login(LoginDto loginDto);

    boolean register(RegisterDto registerDto);
}
