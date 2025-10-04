package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean register(RegisterDto register) {
        // Проверяем, есть ли пользователь с таким email
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }

        User user = new User();
        user.setEmail(register.getUsername());
        user.setPassword(encoder.encode(register.getPassword()));
        user.setFirstName(register.getFirstName() != null ? register.getFirstName() : "");
        user.setLastName(register.getLastName() != null ? register.getLastName() : "");
        user.setPhone(register.getPhone() != null ? register.getPhone() : "");

        Role role = Role.USER;
        if (register.getRole() != null && !register.getRole().isBlank()) {
            role = Role.valueOf(register.getRole().toUpperCase());
        }
        user.setRole(role);

        userRepository.save(user);
        return true;
    }
}