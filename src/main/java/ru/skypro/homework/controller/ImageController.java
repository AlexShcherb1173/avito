package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.impl.ImageService;

// ImageController.java
// ImageController.java
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // GET /images/ads/filename.jpg
    @GetMapping("/images/ads/{filename:.+}")
    public ResponseEntity<Resource> getAdImage(@PathVariable String filename) {
        try {
            Resource file = imageService.loadAsResource(filename, "ads");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /images/users/{filename}
    @GetMapping("/images/users/{filename:.+}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String filename) {
        try {
            Resource file = imageService.loadAsResource(filename, "users");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}