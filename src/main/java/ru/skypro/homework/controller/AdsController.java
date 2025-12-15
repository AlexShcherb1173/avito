package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.Collections;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201).body(new Ad());
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok(new Ad());
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data", produces = "application/octet-stream")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]);
    }
}

