package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skypro.homework.entity.Role.USER;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserServiceImpl userServiceImpl;

    UserDto userDto = UserDto.builder().id(1).email("user@example.com").firstName("Ivan").lastName("Ivanov").phone("+7 (999) 111-22-33").role(USER).image("src/test/resources/pictures/bee.jpg").build();
    NewPasswordDto newPasswordDto = NewPasswordDto.builder().currentPassword("currentPass1").newPassword("newPass123").build();
    UpdateUserDto updateUserDto = UpdateUserDto.builder().firstName("Ivan").lastName("Petrov").phone("+7 (999) 123-45-67").build();

    public UserControllerTest() throws IOException {
    }

    @Test
    @WithMockUser(roles = "USER")
    void setPassword_shouldCallService() throws Exception {
        Mockito.doNothing().when(userServiceImpl).setPassword(any(NewPasswordDto.class));
        String newPasswordDtoJson = objectMapper.writeValueAsString(newPasswordDto);
        mockMvc.perform(post("/users/set_password").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(newPasswordDtoJson)).andExpect(status().isOk());

        Mockito.verify(userServiceImpl).setPassword(newPasswordDto);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUser_shouldReturnUserDto() throws Exception {
        Mockito.when(userServiceImpl.getCurrentUserInformation()).thenReturn(userDto);

        mockMvc.perform(get("/users/me").with(csrf()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.email").value("user@example.com"));

        Mockito.verify(userServiceImpl).getCurrentUserInformation();
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUser_shouldReturnUpdatedDto() throws Exception {
        Mockito.when(userServiceImpl.updateUserInformation(any(UpdateUserDto.class))).thenReturn(updateUserDto);

        mockMvc.perform(patch("/users/me").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateUserDto)).with(csrf())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.firstName").value("Ivan")).andExpect(jsonPath("$.lastName").value("Petrov")).andExpect(jsonPath("$.phone").value("+7 (999) 123-45-67"));
        Mockito.verify(userServiceImpl).updateUserInformation(updateUserDto);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserImage_shouldCallServiceWithMultipart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "avatar.png", MediaType.IMAGE_PNG_VALUE, "fake image".getBytes());
        Mockito.doNothing().when(userServiceImpl).uploadUserPicture(file);

        mockMvc.perform(multipart("/users/me/image").file(file).with(csrf()).with(request -> {
            request.setMethod("PATCH");
            return request;
        })).andExpect(status().isOk());
        Mockito.verify(userServiceImpl).uploadUserPicture(file);
    }
}