package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_ShouldReturnTrue_WhenCredentialsAreValid() {
        String username = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setPassword(encodedPassword);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        boolean result = authService.login(username, password);

        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void login_ShouldReturnFalse_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        boolean result = authService.login(username, password);

        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(username);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void login_ShouldReturnFalse_WhenPasswordIsInvalid() {
        String username = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setPassword(encodedPassword);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        boolean result = authService.login(username, password);

        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void register_ShouldReturnTrue_WhenRegistrationIsSuccessful() {
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+79999999999");
        register.setRole(Role.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());

        when(userRepository.existsByEmail(register.getUsername())).thenReturn(false);
        when(userMapper.toEntity(register)).thenReturn(userEntity);
        when(passwordEncoder.encode(register.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        boolean result = authService.register(register);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(register.getUsername());
        verify(userRepository, times(1)).save(userEntity);
        assertEquals("encodedPassword", userEntity.getPassword());
    }

    @Test
    void register_ShouldReturnFalse_WhenUserAlreadyExists() {
        Register register = new Register();
        register.setUsername("existing@example.com");
        register.setPassword("password");

        when(userRepository.existsByEmail(register.getUsername())).thenReturn(true);

        boolean result = authService.register(register);

        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail(register.getUsername());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldSetUserRole_WhenRoleIsNull() {
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setRole(null);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(register.getUsername());

        when(userRepository.existsByEmail(register.getUsername())).thenReturn(false);
        when(userMapper.toEntity(register)).thenReturn(userEntity);
        when(passwordEncoder.encode(register.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        boolean result = authService.register(register);

        assertTrue(result);
        assertEquals(Role.USER, userEntity.getRole());
    }

    @Test
    void register_ShouldReturnFalse_WhenExceptionOccurs() {
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("password");

        when(userRepository.existsByEmail(register.getUsername())).thenReturn(false);
        when(userMapper.toEntity(register)).thenThrow(new RuntimeException("Database error"));

        boolean result = authService.register(register);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}