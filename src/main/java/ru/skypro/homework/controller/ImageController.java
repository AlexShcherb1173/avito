package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;


    private final String UPLOAD_DIR = "./uploads/";

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) throws IOException {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));


        Path imagePath = Paths.get(UPLOAD_DIR + image.getFilePath());
        if (!Files.exists(imagePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image file not found on disk");
        }


        MediaType mediaType = Optional.ofNullable(image.getMediaType())
                .map(MediaType::parseMediaType)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);


        byte[] imageBytes = Files.readAllBytes(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(imageBytes.length);
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).getHeaderValue());
        headers.set("Content-Disposition", "inline; filename=\"" + image.getFilePath() + "\"");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}