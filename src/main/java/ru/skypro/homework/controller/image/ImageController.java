package ru.skypro.homework.controller.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    @GetMapping("/storage/user_images/{filename}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String filename) {
        // Заглушка: возвращаем тестовую картинку
        byte[] stubImage = new byte[10]; // Миниатюрная заглушка
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(stubImage);
    }
}