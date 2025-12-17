package ru.skypro.homework.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_ExistingUser_ShouldReturnDto() {
        // Given
        String email = "test@example.com";
        UserEntity testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail(email);
        testUser.setFirstName("Ivan");
        testUser.setLastName("Ivanov");
        testUser.setRole(Role.USER);

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail(email);
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setRole("USER");


        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(userDto);

        // When
        UserDto result = userService.getUser(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Ivan", result.getFirstName());
    }

    @Test
    void getUser_NonExistingUser_ShouldThrowException() {
        // Given
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> userService.getUser("unknown@mail.com"));
    }

    @Test
    void updatePassword_CorrectPassword_ShouldUpdate() {
        // Given
        UserEntity user = new UserEntity();
        user.setPassword("encodedOldPass");

        NewPasswordDto dto = new NewPasswordDto();
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        // When
        boolean result = userService.updatePassword(dto, "test@example.com");

        // Then
        assertTrue(result);
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void updateUser_ShouldUpdateAndReturnDto() {
        // Given
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("OldName");
        user.setLastName("OldLastName");
        user.setPhone("89140001122");

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setFirstName("NewName");
        updateDto.setLastName("NewLastName");
        updateDto.setPhone("89993335577");

        UpdateUserDto expectedResult = new UpdateUserDto();
        expectedResult.setFirstName("NewName");
        expectedResult.setLastName("NewLastName");
        expectedResult.setPhone("89993335577");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        // When
        UpdateUserDto result = userService.updateUser(updateDto, email);

        // Then
        assertNotNull(result);
        verify(userMapper).updateEntityFromDto(updateDto, user);
        verify(userRepository).save(user);
    }
}
