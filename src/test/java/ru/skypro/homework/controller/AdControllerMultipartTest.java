package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdControllerMultipartTest {

    @Autowired MockMvc mockMvc;

    @Test
    @WithMockUser
    void addAd_multipart_201() throws Exception {
        MockMultipartFile props = new MockMultipartFile(
                "properties", "", "application/json",
                "{ \"title\":\"Кресло\", \"price\":9000, \"description\":\"Мягкое\" }".getBytes()
        );
        MockMultipartFile image = new MockMultipartFile(
                "image", "ph.png", "image/png", new byte[]{1,2,3}
        );

        mockMvc.perform(multipart("/ads")
                        .file(props)
                        .file(image))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value("Кресло"))
                .andExpect(jsonPath("$.price").value(9000));
    }
    @Test
    @WithMockUser
    void updateImage_patchMultipart_200() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "new.jpg", "image/jpeg", new byte[]{9,8,7}
        );

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/octet-stream"));
    }

}
