package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {

        return ResponseEntity.ok().body(adService.getAllAds());
    }

    @PostMapping
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") CreateOrUpdateAdDto dto,
                                       @RequestPart("image") MultipartFile multipartFile,
                                       @AuthenticationPrincipal UserDetails details) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adService.createAd(dto, multipartFile, details));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAdInfo(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable int id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable int id,
                                          @RequestBody CreateOrUpdateAdDto adDto) {
        return ResponseEntity.ok(adService.updateAd(id, adDto));
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDto> getMyAds(@AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(adService.getMyAds(userDetails));
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable int id,
                                              @RequestPart MultipartFile image) {
        adService.updateAdImage(id,image);
        return ResponseEntity.ok().build();
    }


}
