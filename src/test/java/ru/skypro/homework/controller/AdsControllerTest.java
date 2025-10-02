package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllAds_ShouldReturnEmptyAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").doesNotExist()) // count = null
                .andExpect(jsonPath("$.results").doesNotExist()); // results = null
    }

    @Test
    @WithMockUser
    void createAd_ShouldReturnEmptyAd() throws Exception {
        CreateOrUpdateAd createAd = new CreateOrUpdateAd();
        createAd.setTitle("New Ad");
        createAd.setPrice(1500);


        mockMvc.perform(post("/ads")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()) // id = null
                .andExpect(jsonPath("$.title").doesNotExist()); // title = null
    }

    @Test
    @WithMockUser
    void getAd_ShouldReturnEmptyAd() throws Exception {
        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()) // id = null
                .andExpect(jsonPath("$.title").doesNotExist()); // title = null
    }

    @Test
    @WithMockUser
    void updateAd_ShouldReturnEmptyAd() throws Exception {
        CreateOrUpdateAd updateRequest = new CreateOrUpdateAd();
        updateRequest.setTitle("Updated Ad");
        updateRequest.setPrice(2000);

        mockMvc.perform(patch("/ads/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()) // id = null
                .andExpect(jsonPath("$.title").doesNotExist()); // title = null
    }

    @Test
    @WithMockUser
    void deleteAd_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}