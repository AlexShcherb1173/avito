package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.security.MyUserPrincipal;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;
    private final UserService userService;

    @Override
    @Transactional
    public boolean login(String userName, String password) {
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    @Transactional
    public boolean register(Register register) {
        UserEntity user = userService.saveNewUser(mapper.toUser(register));
        return user != null;
    }
}
