package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Негативный тест: регистрация с невалидным логином")
    void register_withInvalidUserName_returnsBadRequest() throws Exception {
        RegisterDto invalidRegister = RegisterDto.builder().username("ab").password("12345678").firstName("Ann").lastName("Bloom").phone("+7 222 333-4444").role(Role.USER).build();

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegister))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Негативный тест: регистрация с невалидным паролем")
    void register_withInvalidPassword_returnsBadRequest() throws Exception {
        RegisterDto invalidRegister = RegisterDto.builder().username("Bloom").password("123456").firstName("Ann").lastName("Bloom").phone("+7 222 333-4444").role(Role.USER).build();

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegister))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Негативный тест: регистрация с невалидным именем")
    void register_withInvalidFirstName_returnsBadRequest() throws Exception {
        RegisterDto invalidRegister = RegisterDto.builder().username("Bloom").password("12345678").firstName("A").lastName("Bloom").phone("+7 222 333-4444").role(Role.USER).build();

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegister))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Негативный тест: регистрация с невалидной фамилией")
    void register_withInvalidLastName_returnsBadRequest() throws Exception {
        RegisterDto invalidRegister = RegisterDto.builder().username("Bloom").password("12345678").firstName("Ann").lastName("B").phone("+7 222 333-4444").role(Role.USER).build();

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegister))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Негативный тест: регистрация с невалидным телефоном")
    void register_withInvalidPhone_returnsBadRequest() throws Exception {
        RegisterDto invalidRegister = RegisterDto.builder().username("Bloom").password("12345678").firstName("Ann").lastName("Bloom").phone("invalid_phone").role(Role.USER).build();

        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRegister))).andExpect(status().isBadRequest());
    }
}