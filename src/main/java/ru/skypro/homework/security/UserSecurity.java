package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.RoleDto;
import ru.skypro.homework.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
//@RequiredArgsConstructor
public class UserSecurity implements UserDetails {
//        private final User user;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
//    }
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserSecurity(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = Stream.of("ROLE_" + user.getRoleDto().name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        log.info("конструктор UserSecurity вызван ");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("гет auth");

        return this.authorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}