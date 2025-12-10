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

/**
 * Реализация {@link ImageService} для работы с файлами изображений.
 * Сохраняет изображения в локальной файловой системе.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${app.image.storage-path:images/}")
    private String imageStoragePath;

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getImage(String filename) {
        try {
            Path path = Paths.get(imageStoragePath + filename);
            if (!Files.exists(path)) {
                log.warn("Image not found: {}", filename);
                throw new RuntimeException("Image not found: " + filename);
            }

            byte[] imageBytes = Files.readAllBytes(path);

            String contentType = determineContentType(filename);
            log.debug("Image loaded: {}, size: {} bytes, content-type: {}",
                    filename, imageBytes.length, contentType);

            return imageBytes;
        } catch (IOException e) {
            log.error("Failed to read image: {}", filename, e);
            throw new RuntimeException("Failed to read image", e);
        }
    }

    /**
     * Определяет Content-Type по расширению файла.
     *
     * @param filename имя файла
     * @return соответствующий MIME-тип
     */
    private String determineContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * {@inheritDoc}
     */
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