package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
/**
 * Контроллер для работы с изображениями объявлений.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/images")
@Tag(name = "Изображения объявлений")
public class AdImageController {
    private final AdService adService;
    /**
     * Конструктор контроллера AdImageController.
     */
    public AdImageController(AdService adService) {
        this.adService = adService;
    }
    /**
     * Загрузка изображения объявления.
     */
    @GetMapping(value = "/{adImageFilename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @Operation(summary = "Загрузка изображения объявления")
    public ResponseEntity<byte[]> getImageFromFs(@PathVariable("adImageFilename") String adImageId) throws IOException {
        return ResponseEntity.ok().body(adService.readAdImageFromFs(adImageId));
    }
}
