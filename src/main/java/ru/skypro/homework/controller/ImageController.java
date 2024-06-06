package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.impl.UserServiceImpl;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {
    private final UserServiceImpl userService;
    private final ImageService imageService;

    public ImageController(UserServiceImpl userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @Operation(
            tags = "Картинки"
    )
    @GetMapping(value = "/images/{id}", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) {
        return ResponseEntity.ok(imageService.getImage(id));
    }
}
