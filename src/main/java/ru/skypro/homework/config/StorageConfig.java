package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Конфигурация хранилища для изображений.
 * Создает необходимые директории для хранения файлов изображений.
 */
@Configuration
public class StorageConfig {

    @Value("${app.image.storage-path:images/}")
    private String storagePath;

    /**
     * Создает директорию для хранения изображений при запуске приложения.
     * Если директория не существует, она будет создана рекурсивно.
     *
     * @throws RuntimeException если не удается создать директорию
     */
    @Bean
    public void createStorageDirectory() {
        try {
            Files.createDirectories(Paths.get(storagePath));
            System.out.println("Storage directory created: " + storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }
}