package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCurrentUserShouldReturn200() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    void setPasswordShouldReturn200() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("newPassword");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserShouldReturn200() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Ivan");
        updateUser.setLastName("Ivanov");
        updateUser.setPhone("123456");

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk());
    }
}