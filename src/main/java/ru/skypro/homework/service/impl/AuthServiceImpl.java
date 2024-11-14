package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            log.warn("Попытка входа с несуществующим пользователем: {}", userName);
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        boolean passwordMatches = encoder.matches(password, userDetails.getPassword());
        if (passwordMatches) {
            log.info("Пользователь {} успешно вошел в систему", userName);
        } else {
            log.warn("Неверный пароль для пользователя {}", userName);
        }
        return passwordMatches;
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            log.warn("Пользователь с таким именем {} уже существует",
                    register.getUsername());
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());
        log.info("Пользователь {} создан с ролью {} ",
                register.getUsername(),
                register.getRole().name());
        log.info("Информация о классе: {}",
                register.getClass());
        return true;
    }

}
