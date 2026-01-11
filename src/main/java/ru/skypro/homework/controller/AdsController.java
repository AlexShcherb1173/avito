package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) {
        String email = authentication.getName();

        String imagePath = (image != null && image.getOriginalFilename() != null)
                ? image.getOriginalFilename()
                : null;

        Ad created = adService.addAd(email, properties, imagePath);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(adService.getAdsByAuthorEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getExtendedAd(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id,
                                         Authentication authentication) {
        String email = authentication.getName();
        adService.deleteAd(id, email);
        return ResponseEntity.noContent().build(); // 204
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                        Authentication authentication) {
        String email = authentication.getName();
        Ad updated = adService.updateAd(id, email, createOrUpdateAd);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data", produces = "application/octet-stream")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestParam("image") MultipartFile image,
                                              Authentication authentication) {
        String email = authentication.getName();

        String imagePath = (image != null && image.getOriginalFilename() != null)
                ? image.getOriginalFilename()
                : null;

        byte[] bytes = adService.updateAdImage(id, email, imagePath);
        return ResponseEntity.ok(bytes);
    }
}
