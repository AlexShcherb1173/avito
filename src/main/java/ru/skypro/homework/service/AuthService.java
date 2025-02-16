package ru.skypro.homework.service;

import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.RegisterDTO;

public interface AuthService {
    boolean login(LoginDTO loginDTO);

    Long registerId(RegisterDTO register);
}
