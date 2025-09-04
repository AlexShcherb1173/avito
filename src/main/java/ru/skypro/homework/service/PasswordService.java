package ru.skypro.homework.service;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import ru.skypro.homework.dto.NewPassword;

public interface PasswordService {
    boolean setPassword(NewPassword newPassword, Authentication authentication);
}
