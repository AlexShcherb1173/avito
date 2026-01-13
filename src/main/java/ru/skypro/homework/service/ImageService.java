package ru.skypro.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    private static final String UPLOAD_DIR = "uploads/";

    public String saveImage(MultipartFile image, String subDirectory) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR + subDirectory);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return filePath.toString();
    }

    public byte[] getImage(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        if (!Files.exists(path)) {
            throw new IOException("Image not found: " + imagePath);
        }
        return Files.readAllBytes(path);
    }

    public void deleteImage(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}