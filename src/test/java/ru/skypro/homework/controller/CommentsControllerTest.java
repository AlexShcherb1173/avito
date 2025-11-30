package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentsController.class)
@Import(TestSecurityConfig.class)
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentsService commentsService;

    @Test
    @WithMockUser
    void getComments_ShouldReturnComments() throws Exception {
        Comments comments = new Comments();
        comments.setCount(2);
        comments.setResults(List.of(new Comment(), new Comment()));

        when(commentsService.getComments(1)).thenReturn(comments);

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser
    void addComment_ShouldReturnComment() throws Exception {
        CreateOrUpdateComment commentDto = new CreateOrUpdateComment();
        commentDto.setText("Test comment");

        Comment expectedComment = new Comment();
        expectedComment.setPk(1);
        expectedComment.setText("Test comment");

        when(commentsService.addComment(eq(1), any(CreateOrUpdateComment.class), any())).thenReturn(expectedComment);

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("Test comment"));
    }

    @Test
    @WithMockUser
    void deleteComment_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1"))
                .andExpect(status().isOk());

        verify(commentsService, times(1)).deleteComment(eq(1), eq(1), any());
    }

    @Test
    @WithMockUser
    void updateComment_ShouldReturnComment() throws Exception {
        CreateOrUpdateComment commentDto = new CreateOrUpdateComment();
        commentDto.setText("Updated comment");

        Comment expectedComment = new Comment();
        expectedComment.setPk(1);
        expectedComment.setText("Updated comment");

        when(commentsService.updateComment(eq(1), eq(1), any(CreateOrUpdateComment.class), any())).thenReturn(expectedComment);

        mockMvc.perform(patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("Updated comment"));
    }
}