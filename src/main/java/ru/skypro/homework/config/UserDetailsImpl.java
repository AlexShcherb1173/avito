package ru.skypro.homework.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Собственная реализация UserDetails, хранит {@link User} и методы по работе с ним.
 */
@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    /**
     * Передаёт роль.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

    }

    /**
     * Передаёт пароль.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Передаёт логин.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Аккаунт не просрочен.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Аккаунт не заблокирован.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Пароль не просрочен.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Аккаунт подключен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
