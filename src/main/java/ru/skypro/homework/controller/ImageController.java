package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.entity.ImageAdEntity;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.ImageAdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageController {
    private final AvatarService avatarService;
    private final ImageAdService imageAdService;
    private final ImageRepository imageRepository;
//    @GetMapping(value = "/image-avatar/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, "image/*"})
//    public byte[] getAvatar(@PathVariable Integer id) {
//        log.info("Вызван метод контролера возращаюший массив байт аватара");
////        return avatarService.getAvatar(id);
//        return null;
//    }


    @GetMapping(value = "/image-ad/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/*"})
    public byte[] getImageAd(@PathVariable Integer id) throws IOException {
        log.info("Вызван метод контролера возращаюший массив байт изображения объявления");
        return imageAdService.getImageAd(id);

    }

//    @GetMapping(value="/image-avatar/{ids}")
//    public ResponseEntity<List<byte[]>> getImages(@PathVariable List<Integer> ids) throws IOException {
//        List<byte[]> images = new ArrayList<>();
//
//        for (Integer id : ids) {
//            ImageAdEntity imageAd = imageRepository.findImageAdByAdId(id).orElseThrow(() -> {
//                log.info("Пользователь не найден", UserNotFoundException.class);
//                return new UserNotFoundException("not");
//
//            });
//            String path = imageAd.getPath();
//            System.out.println(path);
//            byte[] imageBytes = Files.readAllBytes(Paths.get(path));
//            images.add(imageBytes);
//        }
//        System.out.println(images.size());
//        return ResponseEntity.ok(images);
//
////        return ResponseEntity.ok()
////                .contentType(MediaType.APPLICATION_OCTET_STREAM)
////                .body(images);
//    }


}