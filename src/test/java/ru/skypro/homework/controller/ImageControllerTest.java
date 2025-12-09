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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
        // Arrange
        String testImageName = "test-image.jpg";
        byte[] imageContent = "fake image content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string("Cache-Control", "no-cache, no-store, must-revalidate"))
                .andExpect(header().string("Pragma", "no-cache"))
                .andExpect(header().string("Expires", "0"))
                .andExpect(header().exists("ETag"))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnNotFound_WhenImageNotExists() throws Exception {
        // Arrange
        String nonExistentImage = "nonexistent.jpg";

        when(imageService.getImage(nonExistentImage))
                .thenThrow(new RuntimeException("Image not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + nonExistentImage))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandlePNGFormat() throws Exception {
        // Arrange
        String testImageName = "test-image.png";
        byte[] imageContent = "fake png content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleGifFormat() throws Exception {
        // Arrange
        String testImageName = "test-image.gif";
        byte[] imageContent = "fake gif content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnOctetStream_ForUnknownFormat() throws Exception {
        // Arrange
        String testImageName = "test-image.unknown";
        byte[] imageContent = "fake content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnNotFound_WhenServiceReturnsNull() throws Exception {
        // Arrange
        String testImageName = "null-image.jpg";

        when(imageService.getImage(testImageName)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleEmptyImageData() throws Exception {
        // Arrange
        String testImageName = "empty-image.jpg";
        byte[] emptyContent = new byte[0];

        when(imageService.getImage(testImageName)).thenReturn(emptyContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().bytes(emptyContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleUppercaseExtensions() throws Exception {
        // Arrange
        String testImageName = "test-image.JPG";
        byte[] imageContent = "fake image content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleJpegExtension() throws Exception {
        // Arrange
        String testImageName = "test-image.jpeg";
        byte[] imageContent = "fake image content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldReturnNotFound_WhenServiceThrowsException() throws Exception {
        // Arrange
        String testImageName = "error-image.jpg";

        when(imageService.getImage(testImageName))
                .thenThrow(new RuntimeException("Some service error"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isNotFound());
    }

    @Test
    void getImage_ShouldReturnUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/test.jpg"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleComplexFilenames() throws Exception {
        // Arrange
        String testImageName = "test-image-123_special_name.png";
        byte[] imageContent = "fake image content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageContent));
    }

    @Test
    @WithMockUser
    void getImage_ShouldIncludeSecurityHeaders() throws Exception {
        // Arrange
        String testImageName = "test-image.jpg";
        byte[] imageContent = "fake image content".getBytes();

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andExpect(header().string("Cache-Control", "no-cache, no-store, must-revalidate"))
                .andExpect(header().string("Pragma", "no-cache"))
                .andExpect(header().string("Expires", "0"));
    }

    @Test
    @WithMockUser
    void getImage_ShouldHandleNullEtagCalculation() throws Exception {
        // Arrange
        String testImageName = "test-image.jpg";
        byte[] imageContent = new byte[0];

        when(imageService.getImage(testImageName)).thenReturn(imageContent);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/images/" + testImageName))
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andExpect(content().bytes(imageContent));
    }
}