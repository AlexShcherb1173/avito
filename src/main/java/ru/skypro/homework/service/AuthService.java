package ru.skypro.homework.service;

import ru.skypro.homework.dto.autoAndReg.Register;/*изначально эта зависимость была на файл в папке dto*/

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);
}
