package ru.skypro.homework;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.controller.AuthController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AuthControllerTest {
    @Test
    public void testLoginCorrect() {
        AuthService authService = mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        Login login = new Login();
        login.setUsername("login");
        login.setPassword("password");
        

        when(authService.login("login", "password")).thenReturn(true);

        ResponseEntity<?> response = authController.login(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).login("login", "password");
    }

    @Test
    public void testLoginUnauthorized() {
        AuthService authService = mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        Login login= new Login();
        login.setPassword("password");
        login.setUsername("login");

        when(authService.login("login", "password")).thenReturn(false);

        ResponseEntity<?> response = authController.login(login);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).login("login", "password");
    }
}
