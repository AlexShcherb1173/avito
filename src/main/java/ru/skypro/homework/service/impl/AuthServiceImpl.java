package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

//отвечает за бизнес-логику аутентификации и регистрации:
//Проверяет, существует ли пользователь (userExists)
//Проверяет логин/пароль (login)
//Регистрирует нового пользователя (register)
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository; // ← работаем напрямую с БД
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String username, String password) {
        System.out.println("PasswordEncoder: " + passwordEncoder);
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public void register(Register dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
