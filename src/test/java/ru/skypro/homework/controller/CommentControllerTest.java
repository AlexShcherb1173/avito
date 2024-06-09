package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-bullseye"))
            .withDatabaseName("integration-tests-db");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.contexts", () -> "prod,test");
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void getComments() {
        mockMvc.perform(get("/ads/{id}/comments", 2))
                .andExpect((status().isOk()));
    }

    @Nested
    public class AddCommentError {
        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void addComment() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("новый комментарий");
            mockMvc.perform(post("/ads/{id}/comments", 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isOk());
        }

        @Test
        @SneakyThrows
        void addCommentUnauthorized() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("попытка написать комментарий");

            mockMvc.perform(post("/ads/{id}/comments", 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DeleteCommentError {
        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void deleteCommentByUserWhoCreate() {
            mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 1))
                    .andExpect((status().isOk()));
        }

        @Test
        @SneakyThrows
        @WithUserDetails("elizabeth.swann@gmail.com")
        void deleteCommentBySomeoneElseUser() {
            mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 2, 2))
                    .andExpect((status().isForbidden()));

        }

        @Test
        @SneakyThrows
        @WithUserDetails("hector.barbossa@gmail.com")
        void deleteCommentByAdmin() {
            mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 2, 2))
                    .andExpect((status().is2xxSuccessful()));
        }
    }

    @Nested
    class UpdateCommentError {
        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void updateCommentByUserWhoCreate() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("обновленный комментарий");

            mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 2, 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text").value("обновленный комментарий"));

        }

        @Test
        @SneakyThrows
        @WithUserDetails("elizabeth.swann@gmail.com")
        void updateCommentBySomeoneElseUser() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("обновленный комментарий");

            mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @SneakyThrows
        @WithUserDetails(value = "james.norrington@gmail.com")
        void updateCommentByAdmin() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("обновленный комментарий админом");

            mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 2, 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class NotFoundError {
        @Test
        @SneakyThrows
        @WithUserDetails("james.norrington@gmail.com")
        void notFoundAd() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("новый комментарий");

            mockMvc.perform(post("/ads/{id}/comments", 200)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        @WithUserDetails("james.norrington@gmail.com")
        void notFoundComment() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("новый комментарий");

            mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 200)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isNotFound());
        }
    }


    @Nested
    class ValidError {
        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void updateComment() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("");

            mockMvc.perform(patch("/ads/2/comments/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void addComment() {
            CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("");
            mockMvc.perform(post("/ads/2/comments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createOrUpdateComment)))
                    .andExpect(status().isUnauthorized());
        }

    }

}