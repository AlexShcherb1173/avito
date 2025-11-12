package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом
        userRepository.deleteAll();

        // Создаем пользователя перед каждым тестом
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setPassword(passwordEncoder.encode("password"));
        userEntity.setFirstName("Ivan");
        userEntity.setLastName("Ivanov");
        userEntity.setPhone("89140001122");
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateUser_ShouldUpdateProfile() {
        // Given
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("UpdatedName");
        updateUserDto.setLastName("UpdatedLastName");
        updateUserDto.setPhone("89993335577");

        // When
        UpdateUserDto result = userService.updateUser(updateUserDto, TEST_EMAIL);

        // Then
        assertNotNull(result);
        assertEquals("UpdatedName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("89993335577", result.getPhone());

        UserEntity userEntity = userRepository.findByEmail(TEST_EMAIL)
                .orElseThrow(() -> new RuntimeException("User not found"));
        assertEquals("UpdatedName", userEntity.getFirstName());
        assertEquals("UpdatedLastName", userEntity.getLastName());
        assertEquals("89993335577", userEntity.getPhone());
    }
}
