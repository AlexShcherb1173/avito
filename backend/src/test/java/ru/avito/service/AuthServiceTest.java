package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.RegisterRequest;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.BadRequestException;
import ru.avito.repository.UserRepository;
import ru.avito.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("user@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Ivan");
        registerRequest.setLastName("Ivanov");
        registerRequest.setPhone("+79990000001");
        registerRequest.setRole(Role.USER);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("user@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void registerShouldSaveUserWhenEmailDoesNotExist() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("user@example.com", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals("Ivan", savedUser.getFirstName());
        assertEquals("Ivanov", savedUser.getLastName());
        assertEquals("+79990000001", savedUser.getPhone());
        assertEquals(Role.USER, savedUser.getRole());
        assertNull(savedUser.getImage());

        verify(userRepository).existsByEmail("user@example.com");
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registerShouldSetDefaultRoleUserWhenRoleIsNull() {
        registerRequest.setRole(null);

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void registerShouldThrowBadRequestWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository).existsByEmail("user@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void loginShouldReturnTrueWhenUserExistsAndPasswordMatches() {
        User user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image(null)
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(true);

        boolean result = authService.login(loginRequest);

        assertTrue(result);
        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder).matches("password123", "encoded-password");
    }

    @Test
    void loginShouldReturnFalseWhenUserExistsButPasswordDoesNotMatch() {
        User user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image(null)
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded-password")).thenReturn(false);

        boolean result = authService.login(loginRequest);

        assertFalse(result);
        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder).matches("password123", "encoded-password");
    }

    @Test
    void loginShouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        boolean result = authService.login(loginRequest);

        assertFalse(result);
        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}