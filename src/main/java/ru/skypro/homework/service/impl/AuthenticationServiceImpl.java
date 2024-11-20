package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.service.AuthenticationService;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Возвращает имя пользователя
        }
        return null; // Если пользователь не аутентифицирован
    }
}
