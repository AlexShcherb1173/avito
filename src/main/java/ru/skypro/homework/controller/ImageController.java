package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("image")
public class ImageController{

    private final ImageServiceImpl imageService;

    public ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image) throws IOException {
        imageService.uploadImage(image);
        return ResponseEntity.ok().build();


    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        log.info("Method downloadImage() in ImageController is used");
        Image image = imageService.getImage(id);
        if (image != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
            headers.setContentLength(image.getData().length);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getData());
        }
        log.error("Image is not found");
        return ResponseEntity.notFound().build();
    }

}
