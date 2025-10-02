package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.filter.BasicAuthCorsFilter;
import ru.skypro.homework.service.AuthService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdsController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BasicAuthCorsFilter.class))
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @WithMockUser
    void getAllAds_ShouldReturnAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void createAd_ShouldReturnAd() throws Exception {
        CreateOrUpdateAd createAd = new CreateOrUpdateAd();
        createAd.setTitle("Test Ad");
        createAd.setPrice(1000);
        createAd.setDescription("Test Description");

        mockMvc.perform(post("/ads")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void getAd_ShouldReturnAd() throws Exception {
        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    @WithMockUser
    void deleteAd_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateAd_ShouldReturnUpdatedAd() throws Exception {
        CreateOrUpdateAd updateAd = new CreateOrUpdateAd();
        updateAd.setTitle("Updated Ad");
        updateAd.setPrice(2000);
        updateAd.setDescription("Updated Description");

        mockMvc.perform(patch("/ads/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }
}