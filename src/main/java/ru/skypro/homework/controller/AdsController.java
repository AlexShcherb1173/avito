package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        // TODO: получить все объявления
        Ads ads = new Ads();
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) {
        // TODO: добавить объявление
        Ad ad = new Ad();
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        // TODO: получить информацию об объявлении по ID
        ExtendedAd extendedAd = new ExtendedAd();
        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {
        // TODO: удалить объявление
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        // TODO: обновить информацию об объявлении
        Ad ad = new Ad();
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        // TODO: получить объявления текущего пользователя
        Ads ads = new Ads();
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer id) {
        // TODO: получить комментарии объявления
        Comments comments = new Comments();
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable("id") Integer id,
            @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        // TODO: добавить комментарий к объявлению
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId) {
        // TODO: удалить комментарий
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("adId") Integer adId,
            @PathVariable("commentId") Integer commentId,
            @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        // TODO: обновить комментарий
        Comment comment = new Comment();
        return ResponseEntity.ok(comment);
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<byte[]> updateImage(
            @PathVariable("id") Integer id,
            @RequestParam("image") MultipartFile image) {
        // TODO: обновить картинку объявления
        return ResponseEntity.ok(new byte[0]);
    }
}