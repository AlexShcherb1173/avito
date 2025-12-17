package ru.skypro.homework.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "oldPassword";
    private static final String NEW_PASSWORD = "newPassword";

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом
        userRepository.deleteAll();

        // Создаем тестового пользователя перед каждым тестом
        UserEntity testUser = new UserEntity();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("89140001122");
        testUser.setRole(Role.USER);

        userRepository.save(testUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getUser_ShouldReturnUserInfo() {
        // When
        UserDto userDto = userService.getUser(TEST_EMAIL);

        // Then
        assertNotNull(userDto);
        assertEquals(TEST_EMAIL, userDto.getEmail());
        assertEquals("Иван", userDto.getFirstName());
        assertEquals("Иванов", userDto.getLastName());
        assertEquals("USER", userDto.getRole());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateUser_ShouldUpdateProfileFields() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Петр");
        updateUserDto.setLastName("Петров");
        updateUserDto.setPhone("89141113355");

        // When
        UpdateUserDto result = userService.updateUser(updateUserDto, TEST_EMAIL);

        // Then
        assertEquals("Петр", result.getFirstName());
        assertEquals("Петров", result.getLastName());
        assertEquals("89141113355", result.getPhone());

        UserEntity updatedUser = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertEquals("Петр", updatedUser.getFirstName());
        assertEquals("Петров", updatedUser.getLastName());
        assertEquals("89141113355", updatedUser.getPhone());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updatePassword_WithCorrectCurrentPassword_ShouldUpdate() {
        // Given
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setCurrentPassword(TEST_PASSWORD);
        newPasswordDto.setNewPassword(NEW_PASSWORD);

        // When
        boolean result = userService.updatePassword(newPasswordDto, TEST_EMAIL);

        // Then
        assertTrue(result, "Пароль должен быть успешно обновлен");

        UserEntity user = userRepository.findByEmail(TEST_EMAIL)
                .orElseThrow(() -> new RuntimeException("User not found: " + TEST_EMAIL));
        assertTrue(passwordEncoder.matches(NEW_PASSWORD, user.getPassword()),
                "Новый пароль должен быть установлен и закодирован правильно");
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updatePassword_WithIncorrectCurrentPassword_ShouldReturnFalse() {
        // Given
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setCurrentPassword("wrongPassword");
        newPasswordDto.setNewPassword(NEW_PASSWORD);

        // When
        boolean result = userService.updatePassword(newPasswordDto, TEST_EMAIL);

        // Then
        assertFalse(result);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateUserImage_ShouldSaveImage() throws IOException {
        // Given
        MultipartFile mockImage = getMockImage();

        // When
        boolean result = userService.updateUserImage(mockImage, TEST_EMAIL);

        // Then
        assertTrue(result);

        UserEntity user = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertNotNull(user.getImage());
        assertTrue(user.getImage().startsWith("avatar_"));
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getUserImageById_WhenUserHasImage_ShouldReturnImage() throws IOException {
        // Given
        MultipartFile mockImage = getMockImage();

        userService.updateUserImage(mockImage, TEST_EMAIL);
        UserEntity user = userRepository.findByEmail(TEST_EMAIL).orElseThrow();

        // When
        byte[] result = userService.getUserImageById(user.getId());

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getUserImageById_WhenUserHasNoImage_ShouldThrowException() {
        // Given - пользователь без изображения
        UserEntity user = userRepository.findByEmail(TEST_EMAIL).orElseThrow();

        // When & Then
        assertThrows(IOException.class, () ->
                userService.getUserImageById(user.getId())
        );
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getUserImageContentTypeById_ShouldReturnCorrectType() throws IOException {
        // Given
        MultipartFile mockImage = getMockImage();

        userService.updateUserImage(mockImage, TEST_EMAIL);
        UserEntity user = userRepository.findByEmail(TEST_EMAIL).orElseThrow();

        // When
        String contentType = userService.getUserImageContentTypeById(user.getId());

        // Then
        assertEquals("image/jpeg", contentType);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void deleteUserImage_ShouldRemoveImage() throws IOException {
        // Given
        MultipartFile mockImage = getMockImage();

        userService.updateUserImage(mockImage, TEST_EMAIL);

        UserEntity userBefore = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertNotNull(userBefore.getImage());

        // When
        boolean result = userService.deleteUserImage(TEST_EMAIL);

        // Then
        assertTrue(result);

        UserEntity userAfter = userRepository.findByEmail(TEST_EMAIL).orElseThrow();
        assertNull(userAfter.getImage());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getUserDto_AfterImageUpload_ShouldContainImageUrl() throws IOException {
        // Given
        MultipartFile mockImage = getMockImage();

        userService.updateUserImage(mockImage, TEST_EMAIL);

        // When
        UserDto userDto = userService.getUser(TEST_EMAIL);

        // Then
        assertNotNull(userDto.getImage());
        assertTrue(userDto.getImage().contains("/users/image/"));
    }

    private MultipartFile getMockImage(){
        byte[] imageContent = "test image content".getBytes();
        return new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                imageContent
        );
    }

}
