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

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void getComments_200() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void addComment_200() throws Exception {
        String body = "{ \"text\":\"Отличное объявление!\" }";

        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").exists())
                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.authorFirstName").exists())
                .andExpect(jsonPath("$.authorImage").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.text").value("Отличное объявление!"));
    }

    @Test
    @WithMockUser
    void updateComment_200() throws Exception {
        String body = "{ \"text\":\"Исправленный текст\" }";

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Исправленный текст"));
    }

    @Test
    @WithMockUser
    void deleteComment_200() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 10))
                .andExpect(status().isOk());
    }
}
