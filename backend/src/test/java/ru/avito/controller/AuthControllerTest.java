package ru.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.avito.config.SecurityConfig;
import ru.avito.dto.auth.LoginRequest;
import ru.avito.dto.auth.RegisterRequest;
import ru.avito.entity.Role;
import ru.avito.exception.BadRequestException;
import ru.avito.exception.GlobalExceptionHandler;
import ru.avito.service.AuthService;
import ru.avito.security.CustomUserDetailsService;
import ru.avito.util.ImagePathUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private ImagePathUtils imagePathUtils;

    @BeforeEach
    void setUp() {
        when(customUserDetailsService.loadUserByUsername(any())).thenReturn(
                new User(
                        "user@example.com",
                        "{noop}password",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );
    }

    @Test
    void registerShouldReturn201WhenPayloadIsValid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("new-user@example.com");
        request.setPassword("password123");
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");
        request.setRole(Role.USER);

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void registerShouldReturn400WhenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing@example.com");
        request.setPassword("password123");
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");
        request.setRole(Role.USER);

        doThrow(new BadRequestException("User with this email already exists"))
                .when(authService)
                .register(any(RegisterRequest.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with this email already exists"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void registerShouldReturn400WhenPayloadIsInvalid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("not-an-email");
        request.setPassword("12");
        request.setFirstName("");
        request.setLastName("");
        request.setPhone("");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("username: Email has invalid format")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("password: Password must contain at least 4 characters")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("firstName: First name must not be blank")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("lastName: Last name must not be blank")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("phone: Phone must not be blank")));
    }

    @Test
    void loginShouldReturn200WhenCredentialsAreValid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("user@example.com");
        request.setPassword("password123");

        when(authService.login(any(LoginRequest.class))).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void loginShouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("user@example.com");
        request.setPassword("wrong-password");

        when(authService.login(any(LoginRequest.class))).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void loginShouldReturn400WhenPayloadIsInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("not-an-email");
        request.setPassword("");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("username: Email has invalid format")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("password: Password must not be blank")));
    }

    @Test
    void registerShouldBeAccessibleWithoutAuthentication() throws Exception {
        String requestBody = """
                {
                  "username": "public@example.com",
                  "password": "password123",
                  "firstName": "Ivan",
                  "lastName": "Ivanov",
                  "phone": "+79990000001",
                  "role": "USER"
                }
                """;

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void loginShouldBeAccessibleWithoutAuthentication() throws Exception {
        String requestBody = """
                {
                  "username": "user@example.com",
                  "password": "password123"
                }
                """;

        when(authService.login(any(LoginRequest.class))).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}

