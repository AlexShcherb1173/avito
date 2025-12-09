package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

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
    void getCurrentUser_ShouldReturnUser_WhenUserExists() {
        String username = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setFirstName("John");

        User expectedUser = new User();
        expectedUser.setEmail(username);
        expectedUser.setFirstName("John");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(expectedUser);

        User result = userService.getCurrentUser(username);

        assertNotNull(result);
        assertEquals(username, result.getEmail());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getCurrentUser(username);
        });
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser() {
        String username = "test@example.com";
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");
        updateUser.setPhone("+78888888888");

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail(username);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setEmail(username);
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");

        User expectedUser = new User();
        expectedUser.setEmail(username);
        expectedUser.setFirstName("Jane");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedUser);

        User result = userService.updateUser(username, updateUser);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(userMapper, times(1)).updateEntityFromDto(updateUser, existingUser);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserImage_ShouldUpdateUserImage() {
        String username = "test@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setImage("old-avatar.jpg");

        // Создаем Mock MultipartFile
        MultipartFile mockImage = new MockMultipartFile(
                "image",
                "new-avatar.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(imageService.saveImage(mockImage)).thenReturn("new-avatar.jpg");
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        userService.updateUserImage(username, mockImage);

        // Проверяем, что deleteImage был вызван для старого изображения
        verify(imageService, times(1)).deleteImage("old-avatar.jpg");
        // Проверяем, что saveImage был вызван для нового изображения
        verify(imageService, times(1)).saveImage(mockImage);
        // Проверяем, что путь к изображению был обновлен
        assertEquals("new-avatar.jpg", userEntity.getImage());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updatePassword_ShouldUpdatePassword_WhenCurrentPasswordIsCorrect() {
        String username = "test@example.com";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setPassword("encodedCurrentPassword");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(currentPassword, "encodedCurrentPassword")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        userService.updatePassword(username, currentPassword, newPassword);

        assertEquals(encodedNewPassword, userEntity.getPassword());
        verify(passwordEncoder, times(1)).matches(currentPassword, "encodedCurrentPassword");
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updatePassword_ShouldThrowException_WhenCurrentPasswordIsIncorrect() {
        String username = "test@example.com";
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);
        userEntity.setPassword("encodedCurrentPassword");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(currentPassword, "encodedCurrentPassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(username, currentPassword, newPassword);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserEntity_ShouldReturnUserEntity_WhenUserExists() {
        String username = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(username);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(expectedUser));

        UserEntity result = userService.getUserEntity(username);

        assertNotNull(result);
        assertEquals(username, result.getEmail());
        verify(userRepository, times(1)).findByEmail(username);
    }

    @Test
    void getUserEntity_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserEntity(username);
        });
    }
}