package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsController.class)
@Import(TestSecurityConfig.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getComments_ShouldReturnComments() throws Exception {
        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").doesNotExist())
                .andExpect(jsonPath("$.results").doesNotExist());
    }

    @Test
    void addComment_ShouldReturnComment() throws Exception {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("This is a test comment");

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist());
    }

    @Test
    void deleteComment_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateComment_ShouldReturnComment() throws Exception {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("Updated comment text");

        mockMvc.perform(patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").doesNotExist());
    }
}