package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${upload.path}")
    private String uploadPath;

    public String saveAdImage(MultipartFile image, Long adId) throws IOException {
        String folder = adId != null ? "ads/" + adId : "temp";
        return saveImage(image, folder);
    }

    public String saveUserImage(MultipartFile image) throws IOException {
        String originalFilename = StringUtils.cleanPath(
                Objects.requireNonNull(image.getOriginalFilename())
        );
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        Path uploadDir = Paths.get(uploadPath, "users");
        Files.createDirectories(uploadDir);

        Path filePath = uploadDir.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/users/image/" + filename;
    }

    public void deleteImage(String path) throws IOException {
        if (path != null) {
            Path filePath = Paths.get(uploadPath).resolve(path.substring(1));
            Files.deleteIfExists(filePath);
        }
    }

    private String saveImage(MultipartFile image, String folder) throws IOException {
        String originalName = image.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        Path path = Paths.get(uploadPath, folder, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());

        return "/" + path.toString().replace("\\", "/");
    }

    public byte[] loadImage(String path) throws IOException {
        Path fullPath = Paths.get(uploadPath).resolve(path.substring(1));
        return Files.readAllBytes(fullPath);
    }
}
