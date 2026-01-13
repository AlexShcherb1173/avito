package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    private static final Logger log = LoggerFactory.getLogger(AdsController.class);
    private final AdService adService;
    private final CommentService commentService;

    public AdsController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Получение всех объявлений");
        Ads ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") @Valid CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image,
            Authentication authentication) throws IOException {
        log.info("Создание нового объявления пользователем: {}", authentication.getName());
        Ad ad = adService.addAd(properties, image, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        log.info("Получение объявления по ID: {}", id);
        ExtendedAd extendedAd = adService.getExtendedAd(id);
        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {
        log.info("Удаление объявления ID: {}", id);
        adService.removeAd(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        log.info("Обновление объявления ID: {}", id);
        Ad ad = adService.updateAd(id, createOrUpdateAd);
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Получение объявлений текущего пользователя: {}", authentication.getName());
        Ads ads = adService.getAdsByUser(authentication.getName());
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        log.info("Получение комментариев для объявления ID: {}", id);
        Comments comments = commentService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CreateOrUpdateComment createOrUpdateComment,
            Authentication authentication) {
        log.info("Добавление комментария к объявлению ID: {} пользователем: {}", id, authentication.getName());
        Comment comment = commentService.addComment(id, createOrUpdateComment, authentication);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId) {
        log.info("Удаление комментария ID: {} из объявления ID: {}", commentId, adId);
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            @Valid @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("Обновление комментария ID: {} в объявлении ID: {}", commentId, adId);
        Comment comment = commentService.updateComment(adId, commentId, createOrUpdateComment);
        return ResponseEntity.ok(comment);
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<byte[]> updateImage(
            @PathVariable("id") Integer id,
            @RequestParam("image") MultipartFile image) throws IOException {
        log.info("Обновление изображения объявления ID: {}", id);
        byte[] imageBytes = adService.updateAdImage(id, image);
        return ResponseEntity.ok(imageBytes);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable("id") Integer id) throws IOException {
        log.info("Получение изображения объявления ID: {}", id);
        byte[] imageBytes = adService.getAdImage(id);
        return ResponseEntity.ok(imageBytes);
    }
}