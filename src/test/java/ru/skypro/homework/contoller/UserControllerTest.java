package ru.skypro.homework.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.service.impl.UserServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Test
    void setPassword_Unauthorized_ShouldReturn401() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\": \"password123\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void setPassword_Authorized_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPassword\": \"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void getAuthUser_Unauthorized_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getAuthUser_Authorized_ShouldReturnUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("testuser");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhone("+7 (900) 123-45-67");
        userDTO.setRole(RoleDTO.USER);
        userDTO.setImage("avatar_url");

        when(userService.getCurrentUser()).thenReturn(userDTO);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 123-45-67"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.image").value("avatar_url"));
    }

    @Test
    @WithMockUser
    void updateUser_Authorized_ShouldReturnUpdatedUser() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("John");
        updateUserDTO.setLastName("Doe");
        updateUserDTO.setPhone("+7 (900) 123-45-67");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1);
        updatedUser.setUsername("testuser");
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");
        updatedUser.setPhone("+7 (900) 123-45-67");
        updatedUser.setRole(RoleDTO.USER);
        updatedUser.setImage("avatar_url");

        when(userService.updateUser(any(UpdateUserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"phone\": \"+7 (900) 123-45-67\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 123-45-67"));
    }

    @Test
    @WithMockUser
    void updateUserImage_Authorized_ShouldReturnUpdatedUser() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("testuser");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPhone("+7 (900) 123-45-67");
        userDTO.setRole(RoleDTO.USER);
        userDTO.setImage("avatar_url");

        when(userService.updateUserImage(any(MultipartFile.class))).thenReturn(userDTO);


        mockMvc.perform(multipart("/users/me/image").file(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.phone").value("+7 (900) 123-45-67"))
                .andExpect(jsonPath("$.role.name").value("USER"))
                .andExpect(jsonPath("$.image").value("avatar_url"));
    }
}
