package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("rawPass", "encodedPass")).thenReturn(true);

        boolean result = authService.login("test@mail.com", "rawPass");

        assertTrue(result);
        verify(userRepository).findByEmail("test@mail.com");
        verify(encoder).matches("rawPass", "encodedPass");
    }

    @Test
    void login_Fail_WrongPassword() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        boolean result = authService.login("test@mail.com", "wrongPass");

        assertFalse(result);
    }

    @Test
    void login_Fail_UserNotFound() {
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        boolean result = authService.login("unknown@mail.com", "anyPass");

        assertFalse(result);
    }

    @Test
    void register_Success() {
        RegisterDto reg = new RegisterDto();
        reg.setUsername("new@mail.com");
        reg.setPassword("rawPass");
        reg.setRole("USER");

        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(encoder.encode("rawPass")).thenReturn("encodedPass");

        boolean result = authService.register(reg);

        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_Fail_UserAlreadyExists() {
        RegisterDto reg = new RegisterDto();
        reg.setUsername("exists@mail.com");
        reg.setPassword("rawPass");
        reg.setRole("USER");

        when(userRepository.findByEmail("exists@mail.com")).thenReturn(Optional.of(new User()));

        boolean result = authService.register(reg);

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }
}