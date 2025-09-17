package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsController.class)
public class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Test
    @WithMockUser
    void getComments_returnCommentsDto() throws Exception {
        CommentDto commentDto = new CommentDto(
                1L,
                "image.png",
                "Max",
                LocalDateTime.of(2023, 1, 1, 10, 0),
                2L,
                "Комментарий");
        CommentsDto commentsDto = new CommentsDto(1, List.of(commentDto));

        given(commentService.getComments(5L)).willReturn(commentsDto);

        mockMvc.perform(get("/ads/{id}/comments", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].pk").value(2))
                .andExpect(jsonPath("$.results[0].text").value("Комментарий"))
                .andExpect(jsonPath("$.results[0].createdAt").value("2023-01-01T10:00:00"));

        verify(commentService).getComments(5L);
    }

    @Test
    @WithMockUser
    void addComments_returnCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto(
                1L,
                null,
                null,
                LocalDateTime.of(2023, 1, 1, 10, 0),
                2L,
                "Комментарий");
        CommentCreateOrUpdateRequest commentCreateOrUpdateRequest = new CommentCreateOrUpdateRequest(
                "Комментарий"
        );

        given(commentService.addComment(5L, commentCreateOrUpdateRequest)).willReturn(commentDto);

        mockMvc.perform(post("/ads/{id}/comments", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateOrUpdateRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(2))
                .andExpect(jsonPath("$.text").value("Комментарий"));

        verify(commentService).addComment(5L, commentCreateOrUpdateRequest);
    }

    @Test
    @WithMockUser
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 2L, 3L)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(commentService).deleteComment(2L, 3L);
    }

    @Test
    @WithMockUser
    void updateComment_returnCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto(
                2L,
                null,
                null,
                LocalDateTime.of(2023, 4, 23, 10, 0, 0),
                5L,
                "Комментарий");
        CommentCreateOrUpdateRequest commentCreateOrUpdateRequest = new CommentCreateOrUpdateRequest(
                "Комментарий");

        given(commentService.updateComment(6L, 10L, commentCreateOrUpdateRequest))
                .willReturn(commentDto);

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 6L, 10L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateOrUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(5))
                .andExpect(jsonPath("$.text").value("Комментарий"));

        verify(commentService).updateComment(6L, 10L, commentCreateOrUpdateRequest);
    }

}
