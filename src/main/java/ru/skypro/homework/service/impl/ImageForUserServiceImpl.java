package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Сервис для работы с изображениями пользователей.
 * Реализует методы для добавления изображений и их обработки.
 */

@Service("userImageService")
@Transactional
public class ImageForUserServiceImpl implements ImageService {

    @Value("${users.image.dir.path}")
    private String imageDir;
    private final ImageRepository imageRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageForUserServiceImpl.class);

    public ImageForUserServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    /**
     * Добавляет изображение для пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param file   Файл изображения.
     * @return Добавленное изображение.
     * @throws IOException Если произошла ошибка при работе с файлом.
     */
    @Override
    public Image addImage(int userId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "File name cannot be null");
        String extension = getExtension(originalFilename);

        LOGGER.info("Filename: {}", originalFilename);
        LOGGER.info("Extension: {}", extension);

        Path filePath = Path.of(imageDir, "user_" + userId + "." + extension);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        Image image = imageRepository.findByFilePathContaining("user_" + userId + ".")
                .orElse(new Image());
        image.setFilePath(filePath.toString());
        image.setFileSize(file.getSize());
        image.setMediaType(file.getContentType());

        imageRepository.save(image);
        LOGGER.info("Image saved: id={}, path={}", image.getId(), image.getFilePath());

        return image;
    }

    /**
     * Получает расширение файла по имени.
     *
     * @param filename Имя файла.
     * @return Расширение файла.
     */
    private String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("Filename must contain an extension");
        }
        return filename.substring(lastDotIndex + 1);
    }
}