package ru.skypro.homework.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdControllerTest.class)
@ActiveProfiles("test")
class AdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdServiceImpl adService;

    @BeforeEach
    void setUp() {
        //AdMapper adMapper = new AdMapper();
        Ad ad1 = new Ad();
        Ad ad2 = new Ad();
        List<Ad> adsList = new ArrayList<>(List.of(ad1, ad2));
    }

    @Test
    @WithMockUser
    @DisplayName("Корректно возвращает список объяввлений")
    void test_getAllAds() throws Exception {
        when(adService.getAllAds()).thenReturn(new AdsDTO());
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").exists());
    }

    @Test
    void addAd() {
    }

    @Test
    void getAd() {
    }
}