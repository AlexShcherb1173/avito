package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JdbcUserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterDto register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }

        manager.createUser(
                User.builder()
                        .passwordEncoder(encoder::encode)
                        .username(register.getUsername())
                        .password(register.getPassword())
                        .roles(register.getRole().name())
                        .build());

        ru.skypro.homework.model.User user = new ru.skypro.homework.model.User();
        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setRole(register.getRole());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());

        userRepository.save(user);

        return true;
    }
}
