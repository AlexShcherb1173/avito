package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Аутентификация пользователя.
     */
    @Override
    public boolean login(String userName, String password) {
        return userRepository.findByUserName(userName)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    /**
     * Внесение нового пользователя в базу данных при регистрации.
     *
     * @param registerReqDto dto с данными пользователя.
     * @param role           роль пользователя.
     * @return true - если регистрация прошла успешно, false - если имя пользователя уже занято.
     */
    @Override
    public boolean register(RegisterReqDto registerReqDto, Role role) {

        if (userRepository.findByUserName(registerReqDto.getUsername()).isPresent()) {
            return false;
        }

        registerReqDto.setRole(role);
        registerReqDto.setPassword(encoder.encode(registerReqDto.getPassword()));
        userRepository.save(userMapper.toUser(registerReqDto));
        return true;
    }
}