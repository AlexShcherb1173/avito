package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/*"})
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            byte[] bytes = imageService.getImageBytes(filename);
            return ResponseEntity.ok(bytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}