package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class UserDTO implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Аккаунт не просрочен
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Пароль не просрочен
    }

    @Override
    public boolean isEnabled() {
        return true; // Аккаунт активен
    }
}