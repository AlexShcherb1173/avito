package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

//    private final UserDetailsManager manager;
//    private final PasswordEncoder encoder;
//
//    public AuthServiceImpl(UserDetailsManager manager,
//                           PasswordEncoder passwordEncoder) {
//        this.manager = manager;
//        this.encoder = passwordEncoder;
//    }

    @Override
    public boolean login(Login login) {
//        if (!manager.userExists(username)) {
//            return false;
//        }
//        UserDetails userDetails = manager.loadUserByUsername(username);
//        return encoder.matches(password, userDetails.getPassword());
        return true;
    }



}
