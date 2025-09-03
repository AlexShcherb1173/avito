package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserDetailsManager userDetailsManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, UserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userDetailsManager = userDetailsManager;
    }


    @Override
    public boolean login(String userName, String password) {
        if (!userDetailsManager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (userRepository.existsByEmail(register.getUsername())) {
            return false;
        }

        UserEntity userEntity = userMapper.toEntity(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(userEntity);

        return true;
    }
}
