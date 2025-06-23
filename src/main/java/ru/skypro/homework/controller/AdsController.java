package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import java.util.Collection;

@Slf4j
@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdsService adService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.status(HttpStatus.FOUND).body(adService.getAllAds());
    }

    @PostMapping
    public ResponseEntity<AdDto> addAd(@RequestBody AdDto adDto, Authentication authentication) {
        log.info("Adding new ad: {}", adDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.addAd(adDto,  authentication));
    }

    @GetMapping("{id}")
    public ResponseEntity<AdDto> getAdsById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(adService.getAdById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AdDto> deleteAds(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(adService.getAdById(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") int id, @RequestBody CreateOrUpdateAd updateAd) {
        return ResponseEntity.status(HttpStatus.OK).body(adService.updateAd(id, updateAd));
    }

    @GetMapping("me")
    public ResponseEntity<Collection<AdDto>> getAdsUser() {
        return ResponseEntity.status(HttpStatus.OK).body(adService.getAdsByUserId());
    }

    @PatchMapping("{id}/image")
    public ResponseEntity<AdDto> updateAdsImage(@PathVariable("id") int id, MultipartFile image) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(adService.updateImageAd(id, image));
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<Comments> getAdsComments(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentService.getAllComments(id));
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<?> addAdsComment(@PathVariable("id") int id) {
        return new  ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteAdsComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> updateAdsComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
