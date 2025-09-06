package ru.skypro.homework.service.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void register(Register dto) {
        if (!StringUtils.hasText(dto.getUsername()) || !StringUtils.hasText(dto.getPassword())) {
            throw new IllegalArgumentException("username/password required");
        }
        users.findByEmail(dto.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("user exists");
        });
        UserEntity u = new UserEntity();
        u.setEmail(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setPhone(dto.getPhone());
        u.setRole(dto.getRole() == null ? Role.USER : Role.valueOf(dto.getRole().name()));
        users.save(u);
    }

    @Override
    public void login(Login dto) {
    }

    @Override
    public void changePassword(String username, NewPassword dto) {
        UserEntity u = users.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("not found"));
        if (!encoder.matches(dto.getCurrentPassword(), u.getPassword())) {
            throw new BadCredentialsException("bad current password");
        }
        u.setPassword(encoder.encode(dto.getNewPassword()));
        users.save(u);
    }
}
