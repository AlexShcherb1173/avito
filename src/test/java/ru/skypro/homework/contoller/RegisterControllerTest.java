package ru.skypro.homework.contoller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void register_ShouldReturnCreated_WhenRegistrationIsSuccessful() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newUser");
        registerDTO.setPassword("password123");
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        registerDTO.setPhone("+7 (123) 456-78-90");
        registerDTO.setRole(RoleDTO.USER);

        when(authService.register(any(RegisterDTO.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenRegistrationFails() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("existingUser");
        registerDTO.setPassword("password123");
        registerDTO.setFirstName("Jane");
        registerDTO.setLastName("Smith");
        registerDTO.setPhone("+7 (987) 654-32-10");
        registerDTO.setRole(RoleDTO.USER);

        when(authService.register(any(RegisterDTO.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }
}
