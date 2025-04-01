package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void setPassword_ShouldReturnNoContent_WhenPasswordIsUpdated() throws Exception {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO("oldPassword", "newPassword");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newPasswordDTO))
                        .principal(authentication))
                .andExpect(status().isNoContent());

        verify(userService).updatePassword(newPasswordDTO, authentication);
    }

    @Test
    void setPassword_ShouldReturnBadRequest_WhenPasswordIsInvalid() throws Exception {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO("", "newPassword");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newPasswordDTO))
                        .principal(authentication))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserInfo_ShouldReturnUserDTO_WhenUserIsAuthenticated() throws Exception {
        UserDTO userDTO = new UserDTO("test@example.com", "John", "Doe", "1234567890");
        when(userService.getUserInfo(authentication)).thenReturn(userDTO);

        mockMvc.perform(get("/users/me")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("1234567890"));

        verify(userService).getUserInfo(authentication);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserIsUpdated() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("John", "Doe", "1234567890");
        UpdateUserDTO updatedUserDTO = new UpdateUserDTO("John", "Doe", "1234567890");

        when(userService.updateUser(any(UpdateUserDTO.class), any(Authentication.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateUserDTO))
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(userService).updateUser(updateUserDTO, authentication);
    }



    @Test
    void updateUserAvatar_ShouldReturnNoContent_WhenAvatarIsUpdated() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "avatar.png", "image/png", "image content".getBytes());

        doNothing().when(userService).updateUserAvatar(any(MultipartFile.class), eq(authentication));

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .principal(authentication))
                .andExpect(status().isNoContent());

        verify(userService).updateUserAvatar(any(MultipartFile.class), eq(authentication));
    }

    @Test
    void downloadAvatarFromFileSystem_ShouldReturnAvatar_WhenAvatarExists() throws Exception {
        // Настройка поведения сервиса
        doNothing().when(userService).downloadAvatarFromFileSystem(anyInt(), any(HttpServletResponse.class));

        // Выполнение запроса
        mockMvc.perform(get("/users/me/image/{id}/get", 1)
                        .principal(authentication))
                .andExpect(status().isOk());

        // Проверка вызова сервиса
        verify(userService).downloadAvatarFromFileSystem(eq(1), any(HttpServletResponse.class));
    }
}
