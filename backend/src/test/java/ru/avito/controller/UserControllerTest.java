package ru.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.avito.config.SecurityConfig;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;
import ru.avito.exception.BadRequestException;
import ru.avito.exception.GlobalExceptionHandler;
import ru.avito.service.UserService;
import ru.avito.security.CustomUserDetailsService;
import ru.avito.util.ImagePathUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private ImagePathUtils imagePathUtils;

    @BeforeEach
    void setUp() {
        when(customUserDetailsService.loadUserByUsername("user@example.com")).thenReturn(
                new User(
                        "user@example.com",
                        "{noop}password123",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

        when(customUserDetailsService.loadUserByUsername("admin@example.com")).thenReturn(
                new User(
                        "admin@example.com",
                        "{noop}admin123",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
        );
    }

    @Test
    void getCurrentUserShouldReturn200ForAuthenticatedUser() throws Exception {
        UserDto dto = new UserDto(
                1,
                "user@example.com",
                "Ivan",
                "Ivanov",
                "+79990000001",
                "USER",
                "/images/users/1/avatar.jpg"
        );

        when(userService.getCurrentUser()).thenReturn(dto);

        mockMvc.perform(get("/users/me")
                        .with(httpBasic("user@example.com", "password123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.phone").value("+79990000001"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.image").value("/images/users/1/avatar.jpg"));
    }

    @Test
    void getCurrentUserShouldReturn401WithoutAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateCurrentUserShouldReturn200WhenPayloadIsValid() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");
        request.setLastName("User");
        request.setPhone("+79991112233");

        UserDto response = new UserDto(
                1,
                "user@example.com",
                "Updated",
                "User",
                "+79991112233",
                "USER",
                "/images/users/1/avatar.jpg"
        );

        when(userService.updateCurrentUser(any(UpdateUserRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/me")
                        .with(httpBasic("user@example.com", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.phone").value("+79991112233"));
    }

    @Test
    void updateCurrentUserShouldReturn400WhenPayloadIsInvalid() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("");
        request.setLastName("");
        request.setPhone("");

        mockMvc.perform(patch("/users/me")
                        .with(httpBasic("user@example.com", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("firstName: First name must not be blank")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("lastName: Last name must not be blank")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("phone: Phone must not be blank")));
    }

    @Test
    void updateCurrentUserShouldReturn401WithoutAuthentication() throws Exception {
        String body = """
                {
                  "firstName": "Updated",
                  "lastName": "User",
                  "phone": "+79991112233"
                }
                """;

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updatePasswordShouldReturn200WhenPayloadIsValid() throws Exception {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("password123");
        request.setNewPassword("newPassword123");

        doNothing().when(userService).updatePassword(any(NewPasswordRequest.class));

        mockMvc.perform(patch("/users/set_password")
                        .with(httpBasic("user@example.com", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void updatePasswordShouldReturn400WhenCurrentPasswordIsWrong() throws Exception {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");

        doThrow(new BadRequestException("Current password is incorrect"))
                .when(userService)
                .updatePassword(any(NewPasswordRequest.class));

        mockMvc.perform(patch("/users/set_password")
                        .with(httpBasic("user@example.com", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Current password is incorrect"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updatePasswordShouldReturn400WhenPayloadIsInvalid() throws Exception {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("");
        request.setNewPassword("12");

        mockMvc.perform(patch("/users/set_password")
                        .with(httpBasic("user@example.com", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("currentPassword: Current password must not be blank")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("newPassword: New password must contain at least 4 characters")));
    }

    @Test
    void updatePasswordShouldReturn401WithoutAuthentication() throws Exception {
        String body = """
                {
                  "currentPassword": "password123",
                  "newPassword": "newPassword123"
                }
                """;

        mockMvc.perform(patch("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUserImageShouldReturn200WhenMultipartIsValid() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        when(userService.updateUserImage(any())).thenReturn(
                new UpdateUserImageResponse("/images/users/1/avatar.jpg")
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(httpBasic("user@example.com", "password123"))
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.image").value("/images/users/1/avatar.jpg"));
    }

    @Test
    void updateUserImageShouldReturn400WhenImagePartIsMissing() throws Exception {
        mockMvc.perform(multipart("/users/me/image")
                        .with(httpBasic("user@example.com", "password123"))
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing request part: image"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateUserImageShouldReturn401WithoutAuthentication() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "avatar.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isUnauthorized());
    }
}

