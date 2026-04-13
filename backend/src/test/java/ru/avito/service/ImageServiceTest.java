package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import ru.avito.exception.FileStorageException;
import ru.avito.service.impl.ImageServiceImpl;
import ru.avito.util.ImagePathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private ImagePathUtils imagePathUtils;
    private ImageServiceImpl imageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        imagePathUtils = new ImagePathUtils(tempDir.toString());
        imageService = new ImageServiceImpl(imagePathUtils);
    }

    @Test
    void shouldSaveAdImage() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "photo.jpg",
                "image/jpeg",
                "content".getBytes()
        );

        String result = imageService.saveAdImage(10, file);

        assertEquals("/images/ads/10/image.jpg", result);
        assertTrue(Files.exists(tempDir.resolve("ads").resolve("10").resolve("image.jpg")));
    }

    @Test
    void shouldSaveUserImage() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "avatar.png",
                "image/png",
                "content".getBytes()
        );

        String result = imageService.saveUserImage(5, file);

        assertEquals("/images/users/5/avatar.png", result);
        assertTrue(Files.exists(tempDir.resolve("users").resolve("5").resolve("avatar.png")));
    }

    @Test
    void shouldDeleteImageIfExists() throws Exception {
        Path dir = tempDir.resolve("users").resolve("1");
        Files.createDirectories(dir);
        Path file = dir.resolve("avatar.jpg");
        Files.writeString(file, "content");

        imageService.deleteImageIfExists("/images/users/1/avatar.jpg");

        assertFalse(Files.exists(file));
        assertFalse(Files.exists(dir));
    }

    @Test
    void shouldDoNothingWhenImagePathIsNull() {
        assertDoesNotThrow(() -> imageService.deleteImageIfExists(null));
    }

    @Test
    void shouldDoNothingWhenImagePathIsBlank() {
        assertDoesNotThrow(() -> imageService.deleteImageIfExists("   "));
    }

    @Test
    void shouldDoNothingWhenPhysicalFileDoesNotExist() {
        assertDoesNotThrow(() -> imageService.deleteImageIfExists("/images/users/999/avatar.jpg"));
    }

    @Test
    void shouldThrowWhenDeleteFails() throws Exception {
        ImagePathUtils mockPathUtils = mock(ImagePathUtils.class);
        ImageServiceImpl service = new ImageServiceImpl(mockPathUtils);

        when(mockPathUtils.resolvePhysicalPathFromImageUrl("/images/users/1/avatar.jpg"))
                .thenThrow(new RuntimeException(new IOException("boom")));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.deleteImageIfExists("/images/users/1/avatar.jpg")
        );

        assertNotNull(exception);
    }
}