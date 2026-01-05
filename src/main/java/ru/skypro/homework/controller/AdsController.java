package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        // TODO: получить все объявления
        return ResponseEntity.ok(adService.getAllAds());
    }


    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestBody CreateOrUpdateAd createAdDto) {
        // TODO: Получить email текущего пользователя из SecurityContext
        String authorEmail = "current.user@example.com";
        return adService.createAd(createAdDto, authorEmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        return adService.getAdById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return adService.deleteAd(id) ?
               ResponseEntity.ok().build() :
               ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd updateAdDto) {
        return adService.updateAd(id, updateAdDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        return commentService.getCommentsByAdId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Integer id,
                                              @RequestBody CreateOrUpdateComment commentDto) {
        // TODO: Получить email текущего пользователя
        String authorEmail = "current.user@example.com"; // Заглушка
        return commentService.addComment(id, commentDto, authorEmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        return commentService.deleteComment(adId, commentId) ?
               ResponseEntity.ok().build() :
               ResponseEntity.notFound().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adId,
                                                 @PathVariable Integer commentId,
                                                 @RequestBody CreateOrUpdateComment commentDto) {
        return commentService.updateComment(adId, commentId, commentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}