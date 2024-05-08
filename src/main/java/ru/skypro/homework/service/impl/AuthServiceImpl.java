package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.rq.user.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    private final UserMapper userMapper;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        UserEntity userEntity = userMapper.registerToUserEntity(register);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        manager.createUser(userEntity);
        return true;
    }





}
