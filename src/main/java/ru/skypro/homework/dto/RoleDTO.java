package ru.skypro.homework.dto;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление, представляющее роли пользователя.
 * Реализует интерфейс {@link GrantedAuthority}, чтобы предоставить роль в качестве авторизации.
 */
public enum RoleDTO implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
