package ru.skypro.homework.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.service.AuthService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValid() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("validUser");
        loginDTO.setPassword("ValidPass123");

        when(authService.login("validUser", "ValidPass123")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"validUser\", \"password\": \"ValidPass123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("invalidUser");
        loginDTO.setPassword("WrongPass");

        when(authService.login("invalidUser", "WrongPass")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"invalidUser\", \"password\": \"WrongPass\"}"))
                .andExpect(status().isUnauthorized());
    }
}