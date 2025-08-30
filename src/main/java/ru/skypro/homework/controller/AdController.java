package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;

@RestController
@RequestMapping("/ads")
public class AdController {

    @GetMapping
    public ResponseEntity<?> getAllAds() {
        // TODO: return service.getAllAds()
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd adDto,
                                    @RequestPart("image") MultipartFile image) {
        // TODO: return service.addAd(adDto, image)
        return ResponseEntity.status(201).body(new Ad());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(new Ad());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd adDto) {
        return ResponseEntity.ok(new Ad());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAdsMe() {
        return ResponseEntity.ok().build();
    }
}