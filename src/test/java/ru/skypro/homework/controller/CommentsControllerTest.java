package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCommentsShouldReturn200() throws Exception {
        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    void addCommentShouldReturn200() throws Exception {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("Test comment");

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCommentShouldReturn200() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCommentShouldReturn200() throws Exception {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("Updated comment");

        mockMvc.perform(patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk());
    }
}