package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login_WithBasicAuth_ShouldReturnOk() throws Exception {
        // Given
        Login login = new Login();
        login.setUsername("user@example.com");
        login.setPassword("password");

        when(authService.login(any(String.class), any(String.class))).thenReturn(true);

        // When & Then - используем Basic Authentication
        mockMvc.perform(post("/auth/login")
                        .with(user("user").password("password").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    void register_WithBasicAuth_ShouldReturnOk() throws Exception {
        // Given
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+123456789");
        register.setRole(ru.skypro.homework.dto.Role.USER);

        when(authService.register(any(Register.class))).thenReturn(true);

        // When & Then - используем Basic Authentication
        mockMvc.perform(post("/auth/register")
                        .with(user("user").password("password").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());
    }

    @Test
    void login_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        // Given
        Login login = new Login();
        login.setUsername("user@example.com");
        login.setPassword("password");

        // When & Then - без аутентификации должен возвращать 401
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        // Given
        Register register = new Register();
        register.setUsername("newuser@example.com");
        register.setPassword("password");

        // When & Then - без аутентификации должен возвращать 401
        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isUnauthorized());
    }
}