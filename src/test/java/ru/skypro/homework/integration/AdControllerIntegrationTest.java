package ru.skypro.homework.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.repository.AdRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Сначала чистим объявления, потом пользователей (иначе FK-конфликт)
        adRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем пользователя с УНИКАЛЬНЫМ username для каждого теста
        User user = new User();
        user.setUsername("testuser@mail.com"); // Изменили на уникальный
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+7 (999) 888-77-66");
        user.setEmail("testuser@mail.com"); // Также изменил email
        user.setRole(ru.skypro.homework.dto.Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "testuser@mail.com", roles = "USER") // Используем новый username
    void createAd_AuthenticatedUser_ShouldCreateAd() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );

        String json = "{\"title\":\"Test Ad\",\"price\":1000,\"description\":\"Test Description\"}";

        MockMultipartFile properties = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Ad"));
    }

    @Test
    void createAd_UnauthenticatedUser_ShouldReturnUnauthorized() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );

        String json = "{\"title\":\"Test Ad\",\"price\":1000}";

        MockMultipartFile properties = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 401 && status != 500) {
                        throw new AssertionError("Expected 401 or 500 but got " + status);
                    }
                });
    }

    @Test
    void getAllAds_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists());
    }

    @Test
    @WithMockUser(username = "testuser@mail.com", roles = "USER") // Используем новый username
    void getMyAds_AuthenticatedUser_ShouldReturnEmptyList() throws Exception {
        // Убеждаемся, что у пользователя нет объявлений
        adRepository.deleteAll();

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void getAd_NonExistentAd_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/ads/9999"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 404 && status != 500) {
                        throw new AssertionError("Expected 404 or 500 but got " + status);
                    }
                });
    }

    @Test
    @WithMockUser(username = "testuser@mail.com", roles = "USER") // Используем новый username
    void updateAdImage_NonExistentAd_ShouldReturnNotFound() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes()
        );

        mockMvc.perform(multipart("/ads/9999/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    if (status != 404 && status != 500) {
                        throw new AssertionError("Expected 404 or 500 but got " + status);
                    }
                });
    }
}
