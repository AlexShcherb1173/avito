package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAt;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdsController {
    @GetMapping
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAd(@RequestBody CreateOrUpdateAd ad, @RequestParam MultipartFile image) {
        return ResponseEntity.ok(new CreateOrUpdateAd());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAds(@PathVariable int id) {
        return ResponseEntity.ok(new ExtendedAt());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd ad) {
        return ResponseEntity.ok(new Ad());
    }

    @GetMapping(value = "/me")
    public ResponseEntity<?> getAdsMe(){
        return ResponseEntity.ok(new Ads());
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestParam MultipartFile image){
        return ResponseEntity.ok("");
    }
}
