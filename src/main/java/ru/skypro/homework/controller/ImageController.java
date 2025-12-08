package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * REST контроллер для получения изображений.
 * Обрабатывает запросы на получение файлов изображений по их именам.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {

    private final ImageService imageService;

    /**
     * Получает изображение по имени файла.
     * Поддерживает форматы JPEG, PNG и GIF.
     * Добавлены заголовки для предотвращения кэширования.
     *
     * @param filename имя файла изображения
     * @return массив байтов изображения с соответствующим Content-Type
     */
    @GetMapping(value = "/images/{filename}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        log.info("Getting image: {}", filename);
        try {
            byte[] imageBytes = imageService.getImage(filename);

            // Определяем Content-Type
            String contentType = getContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    // Заголовки для предотвращения кэширования
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .header("Expires", "0")
                    // ETag для валидации (хэш от содержимого)
                    .header("ETag", "\"" + calculateETag(imageBytes) + "\"")
                    .body(imageBytes);
        } catch (Exception e) {
            log.error("Error getting image: {}", filename, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Определяет Content-Type по имени файла.
     */
    private String getContentType(String filename) {
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (filename.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (filename.toLowerCase().endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    /**
     * Вычисляет ETag для контента.
     * Используется для валидации кэша.
     */
    private String calculateETag(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(content);
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(System.currentTimeMillis());
        }
    }
}