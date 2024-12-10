package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageAdEntity;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.ImageAdService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
//@RequestMapping("/images")
public class ImageController {

    private final AvatarService avatarService;
    private final ImageAdService imageAdService;



    @GetMapping(value = "/users/me/image", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getAvatar() throws IOException {
        log.info("Вызван метод контролера возращаюший массив байт аватара");
//        System.out.println("id " + id);
//        image2.getOriginalFilename();

        return avatarService.getAvatar();
    }

    @GetMapping(value = "/ads/img/{adId}", produces = {MediaType.IMAGE_PNG_VALUE, "image/*"})
    public byte[] getImageAd(@PathVariable Integer adId) throws IOException {
        log.info("Вызван метод контролера возращаюший массив байт изображения объявления");
        System.out.println(adId);
        return imageAdService.getImageAd(adId);
    }
}