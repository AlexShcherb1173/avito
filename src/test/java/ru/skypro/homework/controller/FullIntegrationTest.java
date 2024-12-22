package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.constant.StaticForTests.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Integration test for Ads API endpoints")
@Tag("Integration")
class FullIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

        @MockitoSpyBean
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String baseUrl = "http://localhost:8080/";

    @Test
    @Order(1)
    void firstTestRegister() throws Exception {
        mockMvc.perform(post(this.baseUrl + "register").content(objectMapper.writeValueAsString(USER_ADMIN_REGISTER_DTO)).contentType(MediaType.APPLICATION_JSON).characterEncoding("ISO-8859-1")).andExpect(status().is(200));//todo why not status 201???
        Assertions.assertTrue(passwordEncoder.matches(USER_ADMIN_PASSWORD, userRepository.findByUsername(USER_ADMIN_EMAIL).orElseThrow().getPassword()));

        mockMvc.perform(post(this.baseUrl + "register").content(objectMapper.writeValueAsString(USER_ADMIN_REGISTER_DTO)).contentType(MediaType.APPLICATION_JSON).characterEncoding("ISO-8859-1")).andExpect(status().is(409));

        mockMvc.perform(get(this.baseUrl + "users/me").with(httpBasic(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD)).characterEncoding("ISO-8859-1").header("Origin", "http://localhost:3000").header("Access-Control-Request-Method", "GET")).andExpect(status().is(200));
        mockMvc.perform(post(this.baseUrl + "login").content(objectMapper.writeValueAsString(new LoginDto(USER_ADMIN_PASSWORD, USER_ADMIN_EMAIL))).contentType(MediaType.APPLICATION_JSON).characterEncoding("ISO-8859-1")).andExpect(status().is(200));
    }
}
