package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.ImageServiceApi;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceApi imageService;

    @GetMapping(
            value = "{id}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    "image/*"
            }
    )
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getData());
    }

}
