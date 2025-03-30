package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.security.CommentSecurityService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ExtendWith(SpringExtension.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentSecurityService commentSecurityService;

    @Test
    @WithMockUser
    public void shouldReturnComments() throws Exception {         // проходит
        when(commentService.getComments(anyInt())).thenReturn(new CommentsDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")                                  // не проходит
    public void shouldAddComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        when(commentService.addComment(anyInt(), any())).thenReturn(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateOrUpdateCommentDTO())))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldDeleteComment() throws Exception {            // проходит
        doNothing().when(commentService).deleteComment(anyInt(), anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/1/comments/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldUpdateComment() throws Exception {            // не проходит
        when(commentSecurityService.isOwner(anyInt())).thenReturn(true);
        when(commentService.updateComment(anyInt(), anyInt(), any())).thenReturn(new CommentDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateOrUpdateCommentDTO())))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}