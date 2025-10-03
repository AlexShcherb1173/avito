package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skypro.homework.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // если хочешь тестить без Security
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getMe_returnsUser_200() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.image").exists());
    }

    @Test
    @WithMockUser
    void updateMe_returnsUpdated_200() throws Exception {
        // тело согласно UpdateUser (firstName/lastName/phone)
        String body = "{ \"firstName\":\"Иван\", \"lastName\":\"Иванов\", \"phone\":\"+7(999)123-45-67\" }";

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.phone").value("+7(999)123-45-67"));
    }

    @Test
    @WithMockUser
    void setPassword_200() throws Exception {
        String body = "{ \"currentPassword\":\"oldPass11\", \"newPassword\":\"newPass11\" }";

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }
}
