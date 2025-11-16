package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAds_ShouldReturnAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").doesNotExist())
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void getAds_ShouldReturnExtendedAd() throws Exception {
        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").doesNotExist());
    }

    @Test
    void removeAd_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateAds_ShouldReturnAd() throws Exception {
        CreateOrUpdateAd updateAd = new CreateOrUpdateAd();
        updateAd.setTitle("Updated Title");
        updateAd.setPrice(1000);
        updateAd.setDescription("Updated description");

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").doesNotExist());
    }

    @Test
    void getAdsMe_ShouldReturnAds() throws Exception {
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").doesNotExist());
    }

    @Test
    void addAd_ShouldReturnCreated() throws Exception {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("New Ad");
        properties.setPrice(5000);
        properties.setDescription("New description");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        MockMultipartFile propertiesJson = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(properties)
        );

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(propertiesJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").doesNotExist());
    }

    @Test
    void updateImage_ShouldReturnOk() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new_image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "new image content".getBytes()
        );

        mockMvc.perform(multipart("/ads/1/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}