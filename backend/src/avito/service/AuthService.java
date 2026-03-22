package ru.avito.service;

import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    boolean login(LoginRequest request);
}