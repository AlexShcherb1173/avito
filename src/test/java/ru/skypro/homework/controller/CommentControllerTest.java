package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommentServiceImpl commentServiceImpl;
    CommentDto commentDto1 = CommentDto.builder().author(123).authorImage("src/test/resources/pictures/glass.jpg").authorFirstName("Иван").createdAt(System.currentTimeMillis()).pk(456).text("Текст тестового комментария1").build();
    CommentDto commentDto2 = CommentDto.builder().author(123).authorImage("src/test/resources/pictures/bee.jpg").authorFirstName("Tom").createdAt(System.currentTimeMillis()).pk(456).text("New text").build();
    CommentsDto commentsDto = CommentsDto.builder().count(2).results(Arrays.asList(commentDto1, commentDto2)).build();
    CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("New text");

    @Test
    @WithMockUser(roles = "USER")
    void addComment_succeeds() throws Exception {
        given(commentServiceImpl.addComment(any(Integer.class), any(CreateOrUpdateCommentDto.class))).willReturn(commentDto2);

        mockMvc.perform(post("/ads/1/comments").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrUpdateCommentDto)).with(csrf())).andExpect(status().isOk()).andExpect(jsonPath("$.text").value("New text"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getComments_returnsData() throws Exception {
        when(commentServiceImpl.getAllCommentByAdsId(123)).thenReturn(commentsDto);

        mockMvc.perform(get("/ads/123/comments").with(csrf())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteComment_adminCanDelete() throws Exception {
        willDoNothing().given(commentServiceImpl).removeComment(1, 2);

        mockMvc.perform(delete("/ads/1/comments/2").with(csrf())).andExpect(status().isOk());
        verify(commentServiceImpl).removeComment(1, 2);
    }

    @Test
    void deleteComment_requiresAuth() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/2")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateComment_userCanUpdate() throws Exception {
        when(commentServiceImpl.updateComment(any(Integer.class), any(Integer.class), any(CreateOrUpdateCommentDto.class))).thenReturn(commentDto2);

        mockMvc.perform(patch("/ads/1/comments/456").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrUpdateCommentDto)).with(csrf())).andExpect(status().isOk()).andExpect(jsonPath("$.text").value("New text"));
    }
}