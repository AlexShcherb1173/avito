package ru.skypro.homework.service;

import ru.skypro.homework.dto.Registration.Register;
import ru.skypro.homework.dto.User.UserDTO;


public interface AuthService {
    UserDTO login(String userName, String password);
    UserDTO register(Register register);
}
