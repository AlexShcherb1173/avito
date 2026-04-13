package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.BadRequestException;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.UserMapper;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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

    private User user;
    private UserDto userDto;
    private UpdateUserRequest updateUserRequest;
    private NewPasswordRequest newPasswordRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-old-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/images/users/1/old-avatar.jpg")
                .build();

        userDto = new UserDto(
                1,
                "user@example.com",
                "Ivan",
                "Ivanov",
                "+79990000001",
                "USER",
                "/images/users/1/old-avatar.jpg"
        );

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName("Petr");
        updateUserRequest.setLastName("Petrov");
        updateUserRequest.setPhone("+79991112233");

        newPasswordRequest = new NewPasswordRequest();
        newPasswordRequest.setCurrentPassword("old-password");
        newPasswordRequest.setNewPassword("new-password");
    }

    @Test
    void getCurrentUserShouldReturnMappedDtoForAuthenticatedUser() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userDto);

            UserDto result = userService.getCurrentUser();

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("user@example.com", result.getEmail());
            assertEquals("Ivan", result.getFirstName());
            assertEquals("Ivanov", result.getLastName());
            assertEquals("+79990000001", result.getPhone());
            assertEquals("USER", result.getRole());
            assertEquals("/images/users/1/old-avatar.jpg", result.getImage());

            verify(userRepository).findByEmail("user@example.com");
            verify(userMapper).toDto(user);
        }
    }

    @Test
    void getCurrentUserShouldThrowNotFoundWhenAuthenticatedUserDoesNotExist() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> userService.getCurrentUser()
            );

            assertEquals("Authenticated user not found", exception.getMessage());
            verify(userRepository).findByEmail("missing@example.com");
            verify(userMapper, never()).toDto(any(User.class));
        }
    }

    @Test
    void updateCurrentUserShouldUpdateFieldsSaveUserAndReturnMappedDto() {
        User savedUser = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-old-password")
                .firstName("Petr")
                .lastName("Petrov")
                .phone("+79991112233")
                .role(Role.USER)
                .image("/images/users/1/old-avatar.jpg")
                .build();

        UserDto updatedDto = new UserDto(
                1,
                "user@example.com",
                "Petr",
                "Petrov",
                "+79991112233",
                "USER",
                "/images/users/1/old-avatar.jpg"
        );

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(userMapper.toDto(savedUser)).thenReturn(updatedDto);

            UserDto result = userService.updateCurrentUser(updateUserRequest);

            assertNotNull(result);
            assertEquals("Petr", result.getFirstName());
            assertEquals("Petrov", result.getLastName());
            assertEquals("+79991112233", result.getPhone());

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User userBeforeSave = userCaptor.getValue();
            assertEquals("Petr", userBeforeSave.getFirstName());
            assertEquals("Petrov", userBeforeSave.getLastName());
            assertEquals("+79991112233", userBeforeSave.getPhone());

            verify(userMapper).toDto(savedUser);
        }
    }

    @Test
    void updateCurrentUserShouldThrowNotFoundWhenAuthenticatedUserDoesNotExist() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> userService.updateCurrentUser(updateUserRequest)
            );

            assertEquals("Authenticated user not found", exception.getMessage());
            verify(userRepository).findByEmail("missing@example.com");
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).toDto(any(User.class));
        }
    }

    @Test
    void updatePasswordShouldEncodeNewPasswordAndSaveUserWhenCurrentPasswordIsCorrect() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("old-password", "encoded-old-password")).thenReturn(true);
            when(passwordEncoder.encode("new-password")).thenReturn("encoded-new-password");

            userService.updatePassword(newPasswordRequest);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertEquals("encoded-new-password", savedUser.getPassword());

            verify(passwordEncoder).matches("old-password", "encoded-old-password");
            verify(passwordEncoder).encode("new-password");
        }
    }

    @Test
    void updatePasswordShouldThrowBadRequestWhenCurrentPasswordIsIncorrect() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("old-password", "encoded-old-password")).thenReturn(false);

            BadRequestException exception = assertThrows(
                    BadRequestException.class,
                    () -> userService.updatePassword(newPasswordRequest)
            );

            assertEquals("Current password is incorrect", exception.getMessage());
            verify(passwordEncoder).matches("old-password", "encoded-old-password");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void updatePasswordShouldThrowBadRequestWhenNewPasswordEqualsCurrentPassword() {
        newPasswordRequest.setCurrentPassword("same-password");
        newPasswordRequest.setNewPassword("same-password");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("same-password", "encoded-old-password")).thenReturn(true);

            BadRequestException exception = assertThrows(
                    BadRequestException.class,
                    () -> userService.updatePassword(newPasswordRequest)
            );

            assertEquals("New password must be different from current password", exception.getMessage());
            verify(passwordEncoder).matches("same-password", "encoded-old-password");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void updatePasswordShouldThrowNotFoundWhenAuthenticatedUserDoesNotExist() {
        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> userService.updatePassword(newPasswordRequest)
            );

            assertEquals("Authenticated user not found", exception.getMessage());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void updateUserImageShouldDeleteOldSaveNewUpdateUserAndReturnResponse() {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "avatar.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(imageService.saveUserImage(1, image)).thenReturn("/images/users/1/new-avatar.jpg");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

            UpdateUserImageResponse response = userService.updateUserImage(image);

            assertNotNull(response);
            assertEquals("/images/users/1/new-avatar.jpg", response.getImage());

            verify(imageService).deleteImageIfExists("/images/users/1/old-avatar.jpg");
            verify(imageService).saveUserImage(1, image);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            assertEquals("/images/users/1/new-avatar.jpg", userCaptor.getValue().getImage());
        }
    }

    @Test
    void updateUserImageShouldDeleteNothingWhenOldImageIsNullAndStillSaveNewImage() {
        user.setImage(null);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "avatar.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(imageService.saveUserImage(1, image)).thenReturn("/images/users/1/new-avatar.jpg");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");
            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

            UpdateUserImageResponse response = userService.updateUserImage(image);

            assertEquals("/images/users/1/new-avatar.jpg", response.getImage());
            verify(imageService).deleteImageIfExists(null);
            verify(imageService).saveUserImage(1, image);
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void updateUserImageShouldThrowNotFoundWhenAuthenticatedUserDoesNotExist() {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "avatar.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> userService.updateUserImage(image)
            );

            assertEquals("Authenticated user not found", exception.getMessage());
            verify(imageService, never()).deleteImageIfExists(any());
            verify(imageService, never()).saveUserImage(anyInt(), any());
            verify(userRepository, never()).save(any(User.class));
        }
    }
}