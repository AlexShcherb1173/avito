package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.exceptions.EmailAlreadyExistsException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_IfEmailIsNew() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("test@mail.com");
        registerDto.setPassword("password");
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.com");
        user.setPassword("encoderPassword");

        when(userRepository.existsByEmail(registerDto.getUsername())).thenReturn(false);
        when(userMapper.toUserEntity(registerDto)).thenReturn(user);
        when(encoder.encode("password")).thenReturn("encodePassword");

        authService.register(registerDto);

        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(dto));
    }
    @Test
    void login() {
    }
}