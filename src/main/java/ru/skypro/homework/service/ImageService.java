package ru.skypro.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private final Path uploadPath;

    public ImageService(@Value("${upload.path}") String uploadPath) throws IOException {
        this.uploadPath = Paths.get(uploadPath).toAbsolutePath();
        Files.createDirectories(this.uploadPath);
    }

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath);
        return fileName;
    }

    public byte[] getImageBytes(String filename) throws IOException {
        Path imagePath = uploadPath.resolve(filename);
        return Files.readAllBytes(imagePath);
    }
}
