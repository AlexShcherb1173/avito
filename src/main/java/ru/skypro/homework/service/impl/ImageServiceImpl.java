package ru.skypro.homework.service.impl;

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
 * Реализация интерфейса {@link ImageService}, предоставляющая операции, связанные с изображениями.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private final Path uploadPath;

    /**
     * Конструктор сервиса изображений.
     *
     * @param uploadPath путь для сохранения загруженных изображений, передается из конфигурации.
     * @throws IOException если не удается создать директорию для загрузки.
     */
    public ImageServiceImpl(@Value("${upload.path}") String uploadPath) throws IOException {
        this.uploadPath = Paths.get(uploadPath).toAbsolutePath();
        Files.createDirectories(this.uploadPath);
    }

    /**
     * Сохраняет изображение и возвращает его уникальное имя файла.
     *
     * @param file загружаемое изображение.
     * @return имя сохраненного файла.
     * @throws IOException если не удается сохранить файл.
     */
    public String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath);
        return fileName;
    }

    /**
     * Получает изображение в виде массива байтов по имени файла.
     *
     * @param filename имя файла изображения.
     * @return массив байтов изображения.
     * @throws IOException если не удается прочитать файл.
     */
    public byte[] getImageBytes(String filename) throws IOException {
        Path imagePath = uploadPath.resolve(filename);
        return Files.readAllBytes(imagePath);
    }
}

