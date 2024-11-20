package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("${image.upload.dir}")
    private String uploadDir;

    // Логика для сохранения изображения
    @Override
    public String saveImage(MultipartFile imageUpdate, String username) throws IOException {
        String originalFilename = imageUpdate.getOriginalFilename(); // Создаем уникальное имя для изображения
        if (originalFilename == null || originalFilename.isEmpty()) {
            log.error("Имя файла не может быть пустым");
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        Path filePath = Paths.get(uploadDir, uniqueFileName); // Определяем путь к файлу

        Files.createDirectories(filePath.getParent()); // Создаем директорию, если она не существует

        try {
            Files.copy(imageUpdate.getInputStream(), filePath); // Сохраняем файл
            log.info("Изображение успешно сохранено {}", uniqueFileName);
        } catch (IOException e) {
            log.error("Ошибка при сохранении изображения {}", e.getMessage());
            throw e;
        }
        return "/images/" + uniqueFileName; // Возвращаем URL для доступа к изображению
    }

    @Override
    public String getExtension(MultipartFile file) {
        log.info("Вошли в метод getExtension сервиса ImageServiceImpl. Получен файл для сохранения: {}",
                file.getOriginalFilename());
        String fileName = file.getOriginalFilename();

        // Проверяем, что имя файла не null и не пусто
        if (fileName == null || fileName.trim().isEmpty()) {
            log.error("Название файла не может быть пустым или null.");
            throw new IllegalArgumentException("Название файла не может быть пустым или null.");
        }

        // Проверяем, содержит ли имя файла точку
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == 0 || dotIndex == fileName.length() - 1) {
            log.error("Название файла не содержит валидного расширения: {}", fileName);
            throw new IllegalArgumentException("Название файла не содержит валидного расширения.");
        }

        // Возвращаем расширение файла
        String extension = fileName.substring(dotIndex + 1);
        log.info("Определено расширение файла: {}", extension);
        return extension;
    }
}
