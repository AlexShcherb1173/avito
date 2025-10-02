package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private User admin;
    private Ad ad;

    @BeforeEach
    void setUp() {
        adRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword(passwordEncoder.encode("pass"));
        user.setRole(Role.USER);
        userRepository.save(user);

        admin = new User();
        admin.setEmail("admin@mail.ru");
        admin.setPassword(passwordEncoder.encode("pass"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        ad = new Ad();
        ad.setTitle("Test Ad");
        ad.setDescription("Test Description");
        ad.setPrice(100);
        ad.setAuthor(user);
        adRepository.save(ad);
    }

    @Test
    void getAllAds_withoutAuth_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void createAd_shouldReturnCreatedAd() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Bike");
        dto.setDescription("New bike");
        dto.setPrice(500);

        mockMvc.perform(post("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bike"))
                .andExpect(jsonPath("$.price").value(500));
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void updateAd_asOwner_shouldReturnUpdatedAd() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Updated");
        dto.setDescription("Updated Desc");
        dto.setPrice(150);

        mockMvc.perform(patch("/ads/" + ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = {"ADMIN"})
    void updateAd_asAdmin_shouldReturnUpdatedAd() throws Exception {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle("Admin Update");
        dto.setDescription("Admin Desc");
        dto.setPrice(200);

        mockMvc.perform(patch("/ads/" + ad.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Admin Update"));
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void deleteAd_asOwner_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = {"ADMIN"})
    void deleteAd_asAdmin_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId()))
                .andExpect(status().isOk());
    }
}