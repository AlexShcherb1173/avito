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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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

    @Nested
    class UserIsAuthorized {
        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void getRegister() {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Jack"))
                    .andExpect(jsonPath("$.lastName").value("Sparrow"))
                    .andExpect(jsonPath("$.role").value("ADMIN"))
                    .andExpect(jsonPath("$.email").value("captain.jack.sparrow@gmail.com"))
                    .andExpect(jsonPath("$.phone").value("+7 (812) 1234567"))
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.image").value(startsWith("/image/")));
        }

        @Test
        @SneakyThrows
        @WithUserDetails("elizabeth.swann@gmail.com")
        void testSetPassword() {
            NewPassword newPassword = new NewPassword("GovernorDaughter", "MsrTurner");
            mockMvc.perform(post("/users/set_password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newPassword)))
                    .andExpect(status().isOk());
        }

        @Test
        @SneakyThrows
        @WithUserDetails("elizabeth.swann@gmail.com")
        void testSetIncorrectPassword() {
            NewPassword newPassword = new NewPassword("Daughter", "MsrTurner");
            mockMvc.perform(post("/users/set_password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newPassword)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @SneakyThrows
        @WithUserDetails("james.norrington@gmail.com")
        void testUpdateUser() {
            UpdateUser updateUser = UpdateUser.builder()
                    .firstName("James1")
                    .lastName("Norrington1")
                    .phone("+7 (154) 4895761")
                    .build();

            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("James1"))
                    .andExpect(jsonPath("$.lastName").value("Norrington1"))
                    .andExpect(jsonPath("$.phone").value("+7 (154) 4895761"));
        }

        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void testUpdateUserImage() {
            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "avatar.png", MediaType.IMAGE_PNG_VALUE,
                    "avatar".getBytes()
            );

            mockMvc.perform(multipart("/users/me/image")
                            .file(imageFile)
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            }))
                    .andExpect(status().isOk());
        }

        @Nested
        class ValidError {
            @Test
            @SneakyThrows
            @WithUserDetails("elizabeth.swann@gmail.com")
            void testSetPassword() {
                NewPassword newPassword = new NewPassword("GovernorDaughters145236", "er");
                mockMvc.perform(post("/users/set_password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPassword)))
                        .andExpect(status().isForbidden());
            }

            @Test
            @SneakyThrows
            @WithUserDetails("james.norrington@gmail.com")
            void testUpdateUser() {
                UpdateUser updateUser = UpdateUser.builder()
                        .build();

                mockMvc.perform(patch("/users/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUser)))
                        .andExpect(status().isUnauthorized());
            }

        }
    }

    @Nested
    class UserIsUnauthorized {
        @Test
        @SneakyThrows
        void getRegister() {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @SneakyThrows
        void testSetPassword() {
            NewPassword newPassword = new NewPassword("GovernorDaughter", "MsrTurner");
            mockMvc.perform(post("/users/set_password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newPassword)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @SneakyThrows
        void testUpdateUser() {
            UpdateUser updateUser = UpdateUser.builder()
                    .firstName("James1")
                    .lastName("Norrington1")
                    .phone("+7 (154) 4895761")
                    .build();

            mockMvc.perform(patch("/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @SneakyThrows
        void testUpdateUserImage() {
            MockMultipartFile imageFile = new MockMultipartFile(
                    "image",
                    "avatar.png", MediaType.IMAGE_PNG_VALUE,
                    "avatar".getBytes()
            );

            mockMvc.perform(multipart("/users/me/image")
                            .file(imageFile)
                            .with(request -> {
                                request.setMethod("PATCH");
                                return request;
                            }))
                    .andExpect(status().isUnauthorized());
        }
    }


}