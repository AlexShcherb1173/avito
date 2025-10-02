package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdControllerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void getAllAds_200() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void getAdsById_200() throws Exception {
        mockMvc.perform(get("/ads/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pk").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.authorFirstName").exists())
                .andExpect(jsonPath("$.authorLastName").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.image").exists());
    }

    @Test
    @WithMockUser
    void updateAd_200() throws Exception {
        String body = "{ \"title\":\"Стол\", \"price\":5000, \"description\":\"Деревянный стол\" }";

        mockMvc.perform(patch("/ads/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").exists())
                .andExpect(jsonPath("$.title").value("Стол"))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.image").exists())
                .andExpect(jsonPath("$.author").exists());
    }

    @Test
    @WithMockUser
    void deleteAd_204() throws Exception {
        mockMvc.perform(delete("/ads/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
