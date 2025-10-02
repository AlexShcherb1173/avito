package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private Ad ad;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setEmail("user@mail.ru");
        user.setPassword(passwordEncoder.encode("pass"));
        user.setRole(Role.USER);
        userRepository.save(user);

        ad = new Ad();
        ad.setTitle("Test Ad");
        ad.setDescription("Test Desc");
        ad.setPrice(100);
        ad.setAuthor(user);
        adRepository.save(ad);

        comment = new Comment();
        comment.setText("Test Comment");
        comment.setAuthor(user);
        comment.setAd(ad);
        commentRepository.save(comment);
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void getComments_shouldReturnList() throws Exception {
        mockMvc.perform(get("/ads/" + ad.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void addComment_shouldReturnCreatedComment() throws Exception {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("New Comment");

        mockMvc.perform(post("/ads/" + ad.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New Comment"));
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void updateComment_asOwner_shouldReturnUpdatedComment() throws Exception {
        CreateOrUpdateCommentDto dto = new CreateOrUpdateCommentDto();
        dto.setText("Updated Comment");

        mockMvc.perform(patch("/ads/" + ad.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Comment"));
    }

    @Test
    @WithMockUser(username = "user@mail.ru", roles = {"USER"})
    void deleteComment_asOwner_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/ads/" + ad.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isOk());
    }
}