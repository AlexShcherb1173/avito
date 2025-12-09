package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.service.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
@Import(TestSecurityConfig.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    void getImage_ShouldReturnImage_WhenImageExists() throws Exception {
        String testImageName = "test-image.jpg";
        byte[] imageContent = "fake image content".getBytes();
        Path imagePath = Paths.get("images/" + testImageName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageContent);

            mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                    .andExpect(header().string("Content-Disposition", "inline; filename=\"" + testImageName + "\""));

        } finally {
            Files.deleteIfExists(imagePath);
        }
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnNotFound_WhenImageNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/images/nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleDifferentImageFormats() throws Exception {
        String testImageName = "test-image.png";
        byte[] imageContent = "fake png content".getBytes();
        Path imagePath = Paths.get("images/" + testImageName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageContent);

            mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_PNG));

        } finally {
            Files.deleteIfExists(imagePath);
        }
    }


    @Test
    @WithMockUser
    void getImage_ShouldHandleGifFormat() throws Exception {
        String testImageName = "test-image.gif";
        byte[] imageContent = "fake gif content".getBytes();
        Path imagePath = Paths.get("images/" + testImageName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageContent);

            mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/gif"));

        } finally {
            Files.deleteIfExists(imagePath);
        }
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnOctetStream_ForUnknownFormat() throws Exception {
        String testImageName = "test-image.unknown";
        byte[] imageContent = "fake content".getBytes();
        Path imagePath = Paths.get("images/" + testImageName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageContent);

            mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        } finally {
            Files.deleteIfExists(imagePath);
        }
    }
}