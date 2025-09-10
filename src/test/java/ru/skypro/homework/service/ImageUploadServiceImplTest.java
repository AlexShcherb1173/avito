package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.service.impl.ImageUploadServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@ExtendWith(MockitoExtension.class)
public class ImageUploadServiceImplTest {


    @InjectMocks
    private ImageUploadServiceImpl imageUploadService;


    @Test
    void getImageForUserOrAd_Success() throws IOException {

        Path path = Path.of("images/" + USER_IMAGE);
        byte[] expected = Files.readAllBytes(path);

        byte[] actual = imageUploadService.getImageForUserOrAd(USER_IMAGE);

        assertNotNull(actual);
        assertNotEquals(0, actual.length);
        assertArrayEquals(expected, actual);
    }


    @Test
    void getImageForUserOrAd_NotFoundException() throws IOException {

        String filePath = "images/99.jpg";

        assertThrows(NotFoundException.class, () -> imageUploadService.getImageForUserOrAd(filePath));
    }


    @Test
    void getImageForUserOrAd2_Success() throws IOException {

        Path path = Path.of("images/" + AD_IMAGE_1);
        byte[] expected = Files.readAllBytes(path);

        Resource resource = imageUploadService.getImageForUserOrAd2(AD_IMAGE_1);

        assertTrue(resource.exists());
        assertArrayEquals(expected, resource.getInputStream().readAllBytes());
        assertEquals(expected.length, resource.getInputStream().readAllBytes().length);
    }

}
