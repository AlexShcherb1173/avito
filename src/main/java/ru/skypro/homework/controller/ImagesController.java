package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImagesService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/images")
public class ImagesController {

    private final ImagesService imagesService;

    public ImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @Value("${ads.image-file-dir}")
    private String imageFileDir;

    @GetMapping("/{uri}")
    public ResponseEntity<byte[]> getImage(@PathVariable String uri) throws IOException {
        String fileName = uri.substring(uri.lastIndexOf('/') + 1);
        byte[] imageData = imagesService.loadFileByName(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
