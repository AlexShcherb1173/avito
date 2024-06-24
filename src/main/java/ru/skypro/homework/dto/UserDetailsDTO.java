package ru.skypro.homework.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Data
public class UserDetailsDTO implements UserDetails{
    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;
}
