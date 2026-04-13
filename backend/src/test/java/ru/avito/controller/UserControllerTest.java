package ru.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;
import ru.avito.exception.BadRequestException;
import ru.avito.exception.GlobalExceptionHandler;
import ru.avito.service.UserService;
import ru.avito.util.ImagePathUtils;

import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, UserControllerTest.TestImagePathConfig.class})
class UserControllerTest {

    @TestConfiguration
    static class TestImagePathConfig {
        @Bean
        @Primary
        ImagePathUtils imagePathUtils() {
            ImagePathUtils mock = Mockito.mock(ImagePathUtils.class);
            Mockito.when(mock.getRootDir()).thenReturn(Paths.get("target/test-images"));
            return mock;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getCurrentUserShouldReturn200() throws Exception {
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

        mockMvc.perform(get("/users/me"))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("firstName: First name must not be blank")))
                .andExpect(jsonPath("$.message", containsString("lastName: Last name must not be blank")))
                .andExpect(jsonPath("$.message", containsString("phone: Phone must not be blank")));
    }

    @Test
    void updatePasswordShouldReturn200WhenPayloadIsValid() throws Exception {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("password123");
        request.setNewPassword("newPassword123");

        doNothing().when(userService).updatePassword(any(NewPasswordRequest.class));

        mockMvc.perform(patch("/users/set_password")
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("currentPassword: Current password must not be blank")))
                .andExpect(jsonPath("$.message", containsString("newPassword: New password must contain at least 4 characters")));
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
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing request part: image"))
                .andExpect(jsonPath("$.status").value(400));
    }
}