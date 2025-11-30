package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Getting all ads");
        Ads ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart("properties") @Valid CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) {
        log.info("Adding new ad by user: {}", authentication.getName());
        try {
            Ad ad = adsService.addAd(properties, image, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(ad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        log.info("Getting extended ad with id: {}", id);
        try {
            ExtendedAd extendedAd = adsService.getExtendedAd(id);
            return ResponseEntity.ok(extendedAd);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id, Authentication authentication) {
        log.info("Removing ad with id: {} by user: {}", id, authentication.getName());
        try {
            adsService.removeAd(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id,
                                        @RequestBody @Valid CreateOrUpdateAd updateAd,
                                        Authentication authentication) {
        log.info("Updating ad with id: {} by user: {}", id, authentication.getName());
        try {
            Ad ad = adsService.updateAd(id, updateAd, authentication.getName());
            return ResponseEntity.ok(ad);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Getting ads for user: {}", authentication.getName());
        try {
            Ads ads = adsService.getAdsByUser(authentication.getName());
            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestParam("image") MultipartFile image,
                                              Authentication authentication) {
        log.info("Updating image for ad with id: {} by user: {}", id, authentication.getName());
        try {
            adsService.updateAdImage(id, image, authentication.getName());
            byte[] imageBytes = adsService.getAdImage(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(imageBytes);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}