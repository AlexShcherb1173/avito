package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Ads;

@Slf4j
@RestController
@RequestMapping("/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<?> getAllAds(@RequestBody Ads ads) {
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAds(@RequestBody Ads ads) {
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAdsById(@PathVariable("id") int id) {
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAds(@PathVariable("id") int id) {
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable("id") int id) {
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("me")
    public ResponseEntity<?> getAdsUser() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PatchMapping("{id}/image")
    public ResponseEntity<?> updateAdsImage(@PathVariable("id") int id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<?> getAdsComments(@PathVariable("id") int id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<?> addAdsComment(@PathVariable("id") int id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
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
