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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-bullseye"))
            .withDatabaseName("integration-tests-db");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.liquibase.contexts", () -> "prod,test");
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void getAllAds() {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails("elizabeth.swann@gmail.com")
    void addAd() {
        MockMultipartFile file = new MockMultipartFile(
                "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        String json = "{ \"title\": \"Hidsdfsdfsdf\", \"price\": 10, \"description\": \"descsdfdsfsdfsdfsdfsd\" }";

        MockMultipartFile properties = new MockMultipartFile(
                "properties", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());

        mockMvc.perform(multipart("/ads")
                        .file(file)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Hidsdfsdfsdf"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void getAdById() {
        mockMvc.perform(get("/ads/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void deleteAd() {
        mockMvc.perform(delete("/ads/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void updateAdInfo() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd("testadeaddde", 100, "testtestadad");
        mockMvc.perform(patch("/ads/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ad)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(ad.getTitle()))
                .andExpect(jsonPath("$.price").value(ad.getPrice()));
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void getAdsByAuthUser() {
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithUserDetails("captain.jack.sparrow@gmail.com")
    void updateImageAds() {
        MockMultipartFile file = new MockMultipartFile(
                "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(file)
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
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void addAd() {
            MockMultipartFile file = new MockMultipartFile(
                    "image", "image.png", MediaType.IMAGE_PNG_VALUE, "image".getBytes());

            String invalidJson = "{ \"title\": \"\", \"price\": -1, \"description\": \"\" }";

            MockMultipartFile properties = new MockMultipartFile(
                    "properties", "", MediaType.APPLICATION_JSON_VALUE, invalidJson.getBytes());

            mockMvc.perform(multipart("/ads")
                            .file(file)
                            .file(properties)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @SneakyThrows
        @WithUserDetails("captain.jack.sparrow@gmail.com")
        void updateAdInfo() {
            CreateOrUpdateAd ad = new CreateOrUpdateAd("", -1, "");
            mockMvc.perform(patch("/ads/{id}", 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(ad)))
                    .andExpect(status().isForbidden());
        }

    }

}
