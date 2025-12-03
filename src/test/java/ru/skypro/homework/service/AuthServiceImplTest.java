package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_CorrectCredentials_ShouldReturnTrue() {
        // Given
        UserEntity user = new UserEntity();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encodedPass")).thenReturn(true);

        // When
        boolean result = authService.login("user@mail.com", "pass123");

        // Then
        assertTrue(result);
    }

    @Test
    void register_NewUser_ShouldSaveUser() {
        // Given
        Register register = new Register();
        register.setUsername("new@mail.com");
        register.setPassword("pass123");
        register.setFirstName("Ivan");
        register.setLastName("Ivanov");

        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        // When
        boolean result = authService.register(register);

        // Then
        assertTrue(result);
        verify(userRepository).save(any(UserEntity.class));
    }
}
