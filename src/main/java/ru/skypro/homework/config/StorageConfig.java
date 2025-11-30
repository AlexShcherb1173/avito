package ru.skypro.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {

    @Value("${app.image.storage-path:images/}")
    private String storagePath;

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