package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Сервис для работы с изображениями.
 *
 * Отвечает за сохранение изображений на сервере
 * и получение изображений из файловой системы.
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${app.images.dir}")
    private String imagesDir;

    /**
     * Сохраняет изображение на сервере.
     *
     * @param file файл изображения
     * @return имя сохраненного файла
     * @throws IOException если произошла ошибка записи файла
     */
    public String saveImage(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Path.of(imagesDir, fileName);

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        return fileName;
    }

    /**
     * Получает изображение из файловой системы.
     *
     * @param fileName имя файла изображения
     * @return массив байтов изображения
     * @throws IOException если файл не найден или произошла ошибка чтения
     */
    public byte[] getImage(String fileName) throws IOException {

        Path path = Path.of(imagesDir, fileName);

        return Files.readAllBytes(path);
    }
}