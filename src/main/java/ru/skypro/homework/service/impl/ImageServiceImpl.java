package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("avatar")
    private String avatarsDir;

    @Override
    public String saveImageToDisk(MultipartFile file, String username) throws IOException {
        log.info("Вошли в метод saveImageToDisk сервиса ImageServiceImpl. Получен файл для сохранения {} " +
                "Вызван метод getExtension для получения типа файла {}", getExtension(file),
                file.getOriginalFilename());
        Path filePath = Path.of(avatarsDir, username + "." + getExtension(file)); // Определяем путь к файлу
        Files.createDirectories((filePath.getParent())); // Создаем директории, если они не существуют
        if (Files.exists(filePath)) {
            log.info("Загружаемый файл с таким именем имелся в базе данных, предыдущий файл удалён");
            Files.delete(filePath); // Удаляем предыдущий файл, если он существует
        }

        try (var is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
             bis.transferTo(bos);
        } catch (IOException e) {
            log.error("Ошибка при сохранении изображения {}: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        }
        log.info("Изображение успешно сохранено {}", filePath);
        return filePath.toString();
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
