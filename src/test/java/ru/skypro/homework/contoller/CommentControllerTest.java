package ru.skypro.homework.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void getComments_ShouldReturnComments() throws Exception {
        CommentDTO comment = new CommentDTO();
        comment.setAuthor(1);
        comment.setAuthorImage("avatar_url");
        comment.setAuthorFirstName("John");
        comment.setCreatedAt(1700000000000L);
        comment.setPk(1);
        comment.setText("This is a comment.");

        List<CommentDTO> commentList = List.of(comment);
        CommentsDTO commentsDTO = new CommentsDTO(commentList.size(),commentList);

        when(commentService.getComments(1)).thenReturn(commentsDTO);

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].author").value(1))
                .andExpect(jsonPath("$.results[0].authorImage").value("avatar_url"))
                .andExpect(jsonPath("$.results[0].authorFirstName").value("John"))
                .andExpect(jsonPath("$.results[0].createdAt").value(1700000000000L))
                .andExpect(jsonPath("$.results[0].pk").value(1))
                .andExpect(jsonPath("$.results[0].text").value("This is a comment."));
    }

    @Test
    @WithMockUser
    void addComment_ShouldReturnCreatedComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(1);
        commentDTO.setAuthorImage("avatar_url");
        commentDTO.setAuthorFirstName("John");
        commentDTO.setCreatedAt(1700000000000L);
        commentDTO.setPk(1);
        commentDTO.setText("This is a comment.");

        when(commentService.addComment(eq(1), any())).thenReturn(commentDTO);

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"This is a comment.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(1))
                .andExpect(jsonPath("$.authorImage").value("avatar_url"))
                .andExpect(jsonPath("$.authorFirstName").value("John"))
                .andExpect(jsonPath("$.createdAt").value(1700000000000L))
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("This is a comment."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteComment_ShouldReturnOk() throws Exception {
        doNothing().when(commentService).deleteComment(1);

        mockMvc.perform(delete("/ads/1/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateComment_ShouldReturnUpdatedComment() throws Exception {
        CommentDTO updatedComment = new CommentDTO();
        updatedComment.setAuthor(1);
        updatedComment.setAuthorImage("avatar_url");
        updatedComment.setAuthorFirstName("John");
        updatedComment.setCreatedAt(1700000000000L);
        updatedComment.setPk(1);
        updatedComment.setText("Updated comment text.");

        when(commentService.updateComment(eq(1), any())).thenReturn(updatedComment);

        mockMvc.perform(patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Updated comment text.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(1))
                .andExpect(jsonPath("$.authorImage").value("avatar_url"))
                .andExpect(jsonPath("$.authorFirstName").value("John"))
                .andExpect(jsonPath("$.createdAt").value(1700000000000L))
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.text").value("Updated comment text."));
    }
}
