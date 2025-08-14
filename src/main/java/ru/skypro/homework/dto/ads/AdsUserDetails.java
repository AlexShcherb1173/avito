package ru.skypro.homework.dto.ads;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.User;

import java.util.Collection;
import java.util.Collections;

@Getter
@ToString
public class AdsUserDetails implements UserDetails {
    private final Integer id;
    private final String username; // Соответствует email из User
    private final String password;
    private final Role role;

    public AdsUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail(); // Используем email как логин
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Или из User, если есть флаг
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Или из User
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // Или user.isActive()
    }
}