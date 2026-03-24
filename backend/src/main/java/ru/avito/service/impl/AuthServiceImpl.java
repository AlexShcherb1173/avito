package ru.avito.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.RegisterRequest;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.BadRequestException;
import ru.avito.repository.UserRepository;
import ru.avito.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getUsername())) {
            throw new BadRequestException("User with this email already exists");
        }

        User user = User.builder()
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .image(null)
                .build();

        userRepository.save(user);
    }

    @Override
    public boolean login(LoginRequest request) {
        return userRepository.findByEmail(request.getUsername())
                .map(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElse(false);
    }
}