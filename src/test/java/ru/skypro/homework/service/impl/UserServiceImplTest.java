package ru.skypro.homework.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private MultipartFile imageFile;
    private UpdateUserDTO updateUserDTO;

    private TestingAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();

        user.setId(5);
        user.setEmail("test@test.ru");
        user.setPhone("123456");
        user.setFirstName("Test Firstname");
        user.setLastName("Test Lastname");
        user.setPassword("456566");
        user.setRole(null);

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("New User");
        updateUserDTO.setLastName("New Last name user");
        updateUserDTO.setPhone("+79213333333");

        authentication = new TestingAuthenticationToken(user.getEmail(), "456566");

        imageFile = mock(MultipartFile.class);
    }

    @Test
    void getUserInfo_ShouldReturnuserDTO_WhenUserExists() {

        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userMapper.toDtoUserDTO(user)).thenReturn(new UserDTO());

        // Act
        UserDTO result = userService.getUserInfo(authentication);

        // Assert
        assertNotNull(result);
        verify(userRepository).findByEmail(authentication.getName());
    }

    @Test
    void getUserInfo_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {

        // Arrange
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(authentication));
    }

    @Test
    void updateUser_ShouldReturnUpdateUserDTO_WhenUserIsUpdated() {
        // Arrange
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userMapper.toDtoUpdateUserDTO(user)).thenReturn(new UpdateUserDTO());

        // Act
        UpdateUserDTO result = userService.updateUser(updateUserDTO, authentication);
        // Assert
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUserDTO, authentication));
    }
}

