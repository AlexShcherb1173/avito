package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.enity.Role;
import ru.skypro.homework.enity.User;
import ru.skypro.homework.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;



    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByUsername(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.existsByUsername(register.getUsername())) {
            return false;
        }
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRoles(Collections.singleton(Role.valueOf(register.getRole().name())));
        userRepository.save(user);
        return true;
    }

}
