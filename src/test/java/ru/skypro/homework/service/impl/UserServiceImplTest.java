package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private ru.skypro.homework.model.User entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new ru.skypro.homework.model.User();
        entity.setId(1);
        entity.setEmail("test@mail.com");
        entity.setPassword("encodedPass");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setPhone("12345");
        entity.setRole(Role.USER);

        when(authentication.getName()).thenReturn("test@mail.com");
    }

    @Test
    void getCurrentUser_Success() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(entity));

        UserDto dto = userService.getCurrentUser(authentication);

        assertEquals("test@mail.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
    }

    @Test
    void setPassword_Success() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(entity));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");

        userService.setPassword(authentication, "newPass");

        assertEquals("encodedNew", entity.getPassword());
        verify(userRepository).save(entity);
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(entity));

        UpdateUserDto updateUser = new UpdateUserDto();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");
        updateUser.setPhone("67890");

        UpdateUserDto result = userService.updateUser(authentication, updateUser);

        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
        assertEquals("67890", entity.getPhone());
        verify(userRepository).save(entity);
        assertEquals(updateUser, result);
    }
}