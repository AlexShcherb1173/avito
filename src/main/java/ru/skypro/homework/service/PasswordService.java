package ru.skypro.homework.service;


import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.NewPassword;

public interface PasswordService {
    boolean setPassword(NewPassword newPassword, Authentication authentication);
}
