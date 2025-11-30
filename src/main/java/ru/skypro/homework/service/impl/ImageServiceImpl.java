package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${app.image.storage-path:images/}")
    private String imageStoragePath;

    @Override
    public String saveImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID() + extension;
            Path path = Paths.get(imageStoragePath + filename);

            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());

            log.info("Image saved: {}", filename);
            return filename;
        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    public byte[] getImage(String filename) {
        try {
            Path path = Paths.get(imageStoragePath + filename);
            if (!Files.exists(path)) {
                log.warn("Image not found: {}", filename);
                throw new RuntimeException("Image not found: " + filename);
            }
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Failed to read image: {}", filename, e);
            throw new RuntimeException("Failed to read image", e);
        }
    }

    @Override
    public void deleteImage(String filename) {
        try {
            Path path = Paths.get(imageStoragePath + filename);
            Files.deleteIfExists(path);
            log.info("Image deleted: {}", filename);
        } catch (IOException e) {
            log.warn("Failed to delete image: {}", filename, e);
        }
    }
}