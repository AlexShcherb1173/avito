package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user = userRepository.save(UserEntity.builder()
                .email("u@mail.com")
                .password(passwordEncoder.encode("oldPass"))
                .firstName("U")
                .lastName("L")
                .role(UserRole.USER)
                .build());
    }

    @Test
    void setPassword_whenCurrentPasswordWrong_shouldThrowForbidden() {
        NewPassword dto = new NewPassword();
        dto.setCurrentPassword("wrong");
        dto.setNewPassword("newPass");

        assertThatThrownBy(() -> userService.setPassword(user.getEmail(), dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("403");
    }

    @Test
    void setPassword_whenCurrentPasswordCorrect_shouldUpdatePassword() {
        NewPassword dto = new NewPassword();
        dto.setCurrentPassword("oldPass");
        dto.setNewPassword("newPass");

        userService.setPassword(user.getEmail(), dto);

        UserEntity updated = userRepository.findByEmail(user.getEmail()).orElseThrow();
        assertThat(passwordEncoder.matches("newPass", updated.getPassword())).isTrue();
    }
}
