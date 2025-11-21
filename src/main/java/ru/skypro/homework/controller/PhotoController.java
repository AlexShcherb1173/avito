package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.impl.RegisterServiceImpl;
import ru.skypro.homework.impl.PhotoServiceImpl;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/photo")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class PhotoController {
    private final PhotoServiceImpl photoService;


    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getPhotoFromSource(@PathVariable Long photoId) throws IOException {
        log.info("Method {}", RegisterServiceImpl.getMethodName());
        return ResponseEntity.ok(photoService.getPhoto(photoId));
    }
}