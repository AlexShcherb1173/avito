package ru.skypro.homework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Представляет пользователя системы безопасности, реализующего интерфейс UserDetails.
 */
public class SecurityUser implements UserDetails {
    private final User user;

    /**
     * Конструктор для создания экземпляра SecurityUser.
     * @param user объект User
     */
    public SecurityUser(User user) {
        this.user = user;
    }

    /**
     * Получает список ролей пользователя.
     * @return список GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    /**
     * Получает пароль пользователя.
     * @return строка с паролем
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Получает имя пользователя (email).
     * @return email пользователя
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Проверяет, включен ли пользователь.
     * @return true, если пользователь активен
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
