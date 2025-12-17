package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByEmail(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            log.warn("User already exists: {}", register.getUsername());
            return false;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());
        userEntity.setPassword(passwordEncoder.encode(register.getPassword())); //хешируем пароль
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());

        if (register.getRole() != null) {
            try {
                userEntity.setRole(Role.valueOf(register.getRole()));
            } catch (IllegalArgumentException e) {
                userEntity.setRole(Role.USER);
            }
        } else {
            userEntity.setRole(Role.USER);
        }

        userRepository.save(userEntity);
        log.info("User registered successfully: {}", register.getUsername());
        return true;
    }

}
