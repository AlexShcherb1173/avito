package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.service.ImageService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final FileStorageConfig fileStorageConfig;

    @Override
    public String saveImage(MultipartFile image, String subDirectory, String begin) throws IOException {
        validateImageFile(image);

        Path directory = Paths.get(fileStorageConfig.getUploadDir(), subDirectory).toAbsolutePath().normalize();
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Генерируем уникальное имя файла
        String fileExtension = getFileExtension(image.getOriginalFilename());
        String fileName = generateFileName(fileExtension, begin);

        // Сохраняем файл
        Path targetLocation = directory.resolve(fileName);
        Files.copy(image.getInputStream(), targetLocation);

        log.info("Image saved: {}", targetLocation);
        return fileName;
    }

    @Override
    public byte[] getImage(String filename, String subDirectory) throws IOException {
        Path directory = Paths.get(fileStorageConfig.getUploadDir(), subDirectory).toAbsolutePath().normalize();
        Path filePath = directory.resolve(filename).normalize();
        log.info("filePath: {}", filePath);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Image not found: " + filePath);
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public String getImageContentType(String filename) {
        if (filename.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (filename.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (filename.toLowerCase().endsWith(".jpeg") || filename.toLowerCase().endsWith(".jpg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public boolean deleteImage(String filename, String subDirectory) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        Path directory = Paths.get(fileStorageConfig.getUploadDir(), subDirectory).toAbsolutePath().normalize();
        Path filePath = directory.resolve(filename).normalize();

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Image deleted: {}", filePath);
            return true;
        }
        return false;
    }

    @Override
    public void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        //проверка размера
        if (file.getSize() > fileStorageConfig.getAvatarMaxSize()) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size: " +
                    fileStorageConfig.getAvatarMaxSize() + " bytes");
        }

        //проверка типа содержимого
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(fileStorageConfig.getAvatarAllowedTypes())
                .contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: " +
                    Arrays.toString(fileStorageConfig.getAvatarAllowedTypes()));
        }
    }

    private String generateFileName(String extension, String begin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);

        return begin + timestamp + (extension != null ? extension : ".jpg");
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ".jpg";  //расширение по умолчанию
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
