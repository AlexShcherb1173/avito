package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.annotation.Resource;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserDetailsManager userDetailsManager;

    @Override
    public boolean login(String userName, String password) {

        if (!userDetailsManager.userExists(userName)) {
            return false;
        }

        UserDetails userDetails =
                userDetailsManager.loadUserByUsername(userName);

        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {

        if (userDetailsManager.userExists(register.getUsername())) {
            return false;
        }

        UserDetails user =
                org.springframework.security.core.userdetails.User
                        .withUsername(register.getUsername())
                        .password(passwordEncoder.encode(register.getPassword()))
                        .roles(register.getRole())   // БЕЗ .name()
                        .build();

        userDetailsManager.createUser(user);

        return true;
    }
}