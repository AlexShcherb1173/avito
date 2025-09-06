package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;

public interface AuthService {
    void register(Register dto);
    void login(Login dto);
    void changePassword(String username, NewPassword dto);
}
