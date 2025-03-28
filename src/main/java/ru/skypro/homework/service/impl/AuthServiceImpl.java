package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.exceptions.EmailAlreadyExistsException;
import ru.skypro.homework.exceptions.IncorrectPasswordException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AuthService;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;


    @Transactional
    @Override
    public void register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getUsername())) {
            throw new EmailAlreadyExistsException("Пользователь с таким email уже зарегистрирован," +
                    "используйте другой email.");
        }
        UserEntity user = userMapper.toUserEntity(registerDto);
        user.setPassword(encoder.encode(registerDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void login(LoginDto loginDto) {
        UserEntity user = userRepository.findByEmail(loginDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Неверный логин или пароль")
        );
        if (!encoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Неверный пароль");
        }

    }

}
