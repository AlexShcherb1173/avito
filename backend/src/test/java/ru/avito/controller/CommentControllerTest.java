package ru.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.avito.dto.comment.CommentDto;
import ru.avito.dto.comment.CommentsResponse;
import ru.avito.dto.comment.CreateOrUpdateCommentRequest;
import ru.avito.exception.ForbiddenException;
import ru.avito.exception.GlobalExceptionHandler;
import ru.avito.exception.NotFoundException;
import ru.avito.service.CommentService;
import ru.avito.util.ImagePathUtils;

import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, CommentControllerTest.TestImagePathConfig.class})
class CommentControllerTest {

    @TestConfiguration
    static class TestImagePathConfig {
        @Bean
        @Primary
        ImagePathUtils imagePathUtils() {
            ImagePathUtils mock = Mockito.mock(ImagePathUtils.class);
            Mockito.when(mock.getRootDir()).thenReturn(Paths.get("target/test-images"));
            return mock;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void getCommentsShouldReturn200() throws Exception {
        CommentDto first = new CommentDto();
        first.setPk(1);
        first.setAuthor(10);
        first.setAuthorFirstName("Ivan");
        first.setText("First comment");
        first.setCreatedAt(System.currentTimeMillis());

        CommentDto second = new CommentDto();
        second.setPk(2);
        second.setAuthor(11);
        second.setAuthorFirstName("Petr");
        second.setText("Second comment");
        second.setCreatedAt(System.currentTimeMillis());

        when(commentService.getComments(1)).thenReturn(new CommentsResponse(2, List.of(first, second)));

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results[0].pk").value(1))
                .andExpect(jsonPath("$.results[0].text").value("First comment"))
                .andExpect(jsonPath("$.results[1].pk").value(2))
                .andExpect(jsonPath("$.results[1].text").value("Second comment"));
    }

    @Test
    void getCommentsShouldReturn404WhenAdNotFound() throws Exception {
        when(commentService.getComments(999)).thenThrow(new NotFoundException("Ad not found"));

        mockMvc.perform(get("/ads/999/comments"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ad not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void addCommentShouldReturn200ForValidRequest() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Created comment text");

        CommentDto dto = new CommentDto();
        dto.setPk(10);
        dto.setAuthor(1);
        dto.setAuthorFirstName("Ivan");
        dto.setText("Created comment text");
        dto.setCreatedAt(System.currentTimeMillis());

        when(commentService.addComment(eq(1), any(CreateOrUpdateCommentRequest.class))).thenReturn(dto);

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(10))
                .andExpect(jsonPath("$.author").value(1))
                .andExpect(jsonPath("$.text").value("Created comment text"));
    }

    @Test
    void addCommentShouldReturn400WhenPayloadIsInvalid() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("short");

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("text: Comment length must be between 8 and 64 characters")));
    }

    @Test
    void addCommentShouldReturn404WhenAdNotFound() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Created comment text");

        when(commentService.addComment(eq(999), any(CreateOrUpdateCommentRequest.class)))
                .thenThrow(new NotFoundException("Ad not found"));

        mockMvc.perform(post("/ads/999/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ad not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateCommentShouldReturn200ForValidRequest() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        CommentDto dto = new CommentDto();
        dto.setPk(10);
        dto.setAuthor(1);
        dto.setAuthorFirstName("Ivan");
        dto.setText("Updated comment text");
        dto.setCreatedAt(System.currentTimeMillis());

        when(commentService.updateComment(eq(1), eq(10), any(CreateOrUpdateCommentRequest.class))).thenReturn(dto);

        mockMvc.perform(patch("/ads/1/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(10))
                .andExpect(jsonPath("$.text").value("Updated comment text"));
    }

    @Test
    void updateCommentShouldReturn403ForForbidden() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        when(commentService.updateComment(eq(1), eq(10), any(CreateOrUpdateCommentRequest.class)))
                .thenThrow(new ForbiddenException("You cannot edit this comment"));

        mockMvc.perform(patch("/ads/1/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cannot edit this comment"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void updateCommentShouldReturn404WhenCommentNotFound() throws Exception {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Updated comment text");

        when(commentService.updateComment(eq(1), eq(999), any(CreateOrUpdateCommentRequest.class)))
                .thenThrow(new NotFoundException("Comment not found"));

        mockMvc.perform(patch("/ads/1/comments/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteCommentShouldReturn204ForSuccess() throws Exception {
        doNothing().when(commentService).deleteComment(1, 10);

        mockMvc.perform(delete("/ads/1/comments/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCommentShouldReturn403ForForbidden() throws Exception {
        doThrow(new ForbiddenException("You cannot delete this comment"))
                .when(commentService)
                .deleteComment(1, 10);

        mockMvc.perform(delete("/ads/1/comments/10"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cannot delete this comment"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void deleteCommentShouldReturn404WhenCommentNotFound() throws Exception {
        doThrow(new NotFoundException("Comment not found"))
                .when(commentService)
                .deleteComment(1, 999);

        mockMvc.perform(delete("/ads/1/comments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"))
                .andExpect(jsonPath("$.status").value(404));
    }
}