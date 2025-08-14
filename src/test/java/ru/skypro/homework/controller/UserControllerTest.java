package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @Test
    void setPassword_ValidCurrentPassword() {
        // Arrange
        Users user = new Users();
        user.setPassword("oldEncoded");

        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("old");
        newPassword.setNewPassword("new");

        when(authentication.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches("old", "oldEncoded")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newEncoded");

        // Act
        ResponseEntity<Void> response = userController.setPassword(newPassword, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newEncoded", user.getPassword());
    }
}