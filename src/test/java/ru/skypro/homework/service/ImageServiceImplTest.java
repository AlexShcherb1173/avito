package ru.skypro.homework.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {
    @Mock
    private FileStorageConfig config;

    private ImageServiceImpl imageService;

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test_uploads");
        when(config.getUploadDir()).thenReturn(tempDir.toString());
        when(config.getAvatarMaxSize()).thenReturn(1024L); // 1KB
        when(config.getAvatarAllowedTypes()).thenReturn(new String[]{"image/jpeg"});

        imageService = new ImageServiceImpl(config);
    }

    @Test
    void saveImage_ValidFile_ShouldSave() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(500L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));
        when(file.isEmpty()).thenReturn(false);

        // When
        String filename = imageService.saveImage(file, "users", "avatar_");

        // Then
        assertNotNull(filename);
        assertTrue(Files.exists(tempDir.resolve("users").resolve(filename)));
    }

}
