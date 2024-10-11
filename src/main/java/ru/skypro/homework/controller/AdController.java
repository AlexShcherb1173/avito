package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.service.AdService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdController {
    private final AdService adsService;

    public AdController(AdService adsService) {
        this.adsService = adsService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        adsService.getAllAds();
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<?> createAd(@RequestBody CreateOrUpdateAd ad, @RequestBody String image) {
        if (adsService.createAd(ad, image)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdInfo(@RequestParam int id) {
        if (adsService.getAdInfo(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@RequestParam int id) {
        if (adsService.deleteAd(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@RequestParam int id, @RequestBody CreateOrUpdateAd updateAd) {
        if (adsService.updateAdInfo(id, updateAd)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAdsOfUser() {
        if (adsService.getAdsOfUser()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<?> updateAd(@RequestParam int id, @RequestBody String image) {
        if (adsService.updateAdImage(id, image)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
