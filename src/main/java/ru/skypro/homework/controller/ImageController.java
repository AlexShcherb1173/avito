package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Изображения", description = "API для работы с изображениями")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Operation(summary = "Обновление аватара пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    }
    )
    @PatchMapping("/users/me/image")
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        log.info("Update user image called");
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление картинки объявления", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    }
    )
    @PatchMapping("/ads/{id}/image")
    public ResponseEntity<?> updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        log.info("Update image called for ad: {}", id);
        return ResponseEntity.ok().build();
    }
}