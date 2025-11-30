package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdsService adsService;

    @Test
    @WithMockUser
    void getAllAds_ShouldReturnAds() throws Exception {
        Ads ads = new Ads();
        ads.setCount(2);
        ads.setResults(List.of(new Ad(), new Ad()));

        when(adsService.getAllAds()).thenReturn(ads);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void getExtendedAd_ShouldReturnExtendedAd() throws Exception {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(1);
        extendedAd.setTitle("Test Ad");

        when(adsService.getExtendedAd(1)).thenReturn(extendedAd);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("Test Ad"));
    }

    @Test
    @WithMockUser
    void addAd_ShouldReturnCreated() throws Exception {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("New Ad");
        properties.setPrice(5000);

        MockMultipartFile image = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "test image content".getBytes()
        );

        MockMultipartFile propertiesJson = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(properties)
        );

        Ad expectedAd = new Ad();
        expectedAd.setPk(1);

        when(adsService.addAd(any(CreateOrUpdateAd.class), any(), any())).thenReturn(expectedAd);

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(propertiesJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void removeAd_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNoContent());

        verify(adsService, times(1)).removeAd(eq(1), any());
    }

    @Test
    @WithMockUser
    void getAdsMe_ShouldReturnUserAds() throws Exception {
        Ads userAds = new Ads();
        userAds.setCount(1);
        userAds.setResults(List.of(new Ad()));

        when(adsService.getAdsByUser(any())).thenReturn(userAds);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }
}