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
import ru.skypro.homework.exception.UserNotFoundException;
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
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        userEntity.setFirstName("Иван");
        userEntity.setLastName("Иванов");
        userEntity.setPhone("89140001122");
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);
    }

    //Тест полного потока пользователя: обновление профиля, смена пароля и работа с аватаром
    @Test
    @WithMockUser(username = "test@example.com")
    void completeUserFlow_UpdateProfilePasswordAndAvatar() throws IOException {

        // 1. Получение пользователя
        UserDto userDto = userService.getUser(TEST_EMAIL);
        assertNotNull(userDto);
        assertEquals((TEST_EMAIL), userDto.getEmail());
        assertEquals("Иван", userDto.getFirstName());
        assertEquals("Иванов", userDto.getLastName());

        // 2. Обновление профиля
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Петр");
        updateUserDto.setLastName("Петров");
        updateUserDto.setPhone("89141113355");

        UpdateUserDto updatedUserDto = userService.updateUser(updateUserDto, TEST_EMAIL);
        assertEquals("Петр", updatedUserDto.getFirstName());
        assertEquals("Петров", updatedUserDto.getLastName());
        assertEquals("89141113355", updatedUserDto.getPhone());

        // 3. Обновление пароля
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setCurrentPassword(TEST_PASSWORD);
        newPasswordDto.setNewPassword(NEW_PASSWORD);

        boolean passwordResult = userService.updatePassword(newPasswordDto, TEST_EMAIL);
        assertTrue(passwordResult, "Пароль должен быть успешно обновлен");

        UserEntity userEntity = userRepository.findByEmail(TEST_EMAIL)
                .orElseThrow(() -> new RuntimeException("User not found: " + TEST_EMAIL));
        assertTrue(passwordEncoder.matches(NEW_PASSWORD, userEntity.getPassword()),
                "Новый пароль должен быть установлен и закодирован правильно");

        // 4. Загрузка аватара
        MultipartFile mockImage = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        boolean avatarUpdated = userService.updateUserImage(mockImage, TEST_EMAIL);
        assertTrue(avatarUpdated, "Аватар должен быть успешно обновлен");

        // 5. Проверка, что аватар загружен и доступен
        byte[] imageBytes = userService.getUserImage(TEST_EMAIL);
        assertNotNull(imageBytes, "Байты изображения не должны быть пустыми");
        assertTrue(imageBytes.length > 0, "Изображение должно содержать контент");

        // 6. Проверка типа контента аватара
        String contentType = userService.getUserImageContentType(TEST_EMAIL);
        assertNotNull(contentType, "Тип контента не должен быть null");
        assertEquals("image/jpeg", contentType);

        // 7. Проверяем, что в DTO появилась ссылка на аватар
        UserDto userWithAvatar = userService.getUser(TEST_EMAIL);
        assertNotNull(userWithAvatar.getImage(), "UserDto должен иметь URL-адрес изображения");
        assertTrue(userWithAvatar.getImage().contains("/users/"),
                "URL аватара должен содержать /users/");
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getUserImage_WhenNoImage_ShouldThrowException() {
        // Пользователь создается без аватара в @BeforeEach
        assertThrows(IOException.class, () -> userService.getUserImage(TEST_EMAIL),
                "Должен вызывать исключение IOException, когда у пользователя нет изображения");
    }
}
