package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdResponseDTO;
import ru.skypro.homework.dto.AdsResponseDTO;
import ru.skypro.homework.dto.CreateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;

@RestController
@RequestMapping("/ads")
public class AdController {

    @GetMapping
    public ResponseEntity<AdsResponseDTO> getAllAds() {
        return ResponseEntity.ok(new AdsResponseDTO());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<AdResponseDTO> addAd(
            @RequestPart("properties") CreateAdDTO properties,
            @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201).body(new AdResponseDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDTO> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(new ExtendedAdDTO());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdResponseDTO> updateAd(
            @PathVariable Integer id,
            @RequestBody CreateAdDTO request) {
        return ResponseEntity.ok(new AdResponseDTO());
    }

    @GetMapping("/me")
    public ResponseEntity<AdsResponseDTO> getMyAds() {
        return ResponseEntity.ok(new AdsResponseDTO());
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<byte[]> updateAdImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]);
    }
}