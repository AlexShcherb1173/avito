package ru.avito.service;

import ru.avito.dto.Register;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);
}
