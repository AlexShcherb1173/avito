package ru.skypro.homework.service;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;

public interface AuthorizationService {
    void addAuthorities(User user, Role role);

    String getAuthorities(User user);
}
