package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAdsShouldReturn200() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    void createAdShouldReturn200() throws Exception {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Test Ad");
        ad.setDescription("Description");
        ad.setPrice(100);

        mockMvc.perform(post("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ad)))
                .andExpect(status().isOk());
    }

    @Test
    void getAdShouldReturn200() throws Exception {
        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAdShouldReturn200() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateAdShouldReturn200() throws Exception {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Updated Ad");
        ad.setDescription("New desc");
        ad.setPrice(200);

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ad)))
                .andExpect(status().isOk());
    }
}