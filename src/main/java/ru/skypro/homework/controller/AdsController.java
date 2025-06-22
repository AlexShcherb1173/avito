package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.impl.LoggingMethodImpl;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;
    
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> addAd(@RequestPart(value = "properties", required = false) CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image,
                                    Authentication authentication) throws IOException {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        return ResponseEntity.ok(adService.addAd(properties, image, authentication));
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Long id) {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        ExtendedAd ad = adService.getAds(id);
        if (ad != null) {
            return ResponseEntity.ok(ad);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity removeAd(@PathVariable("id") Long adId) throws IOException {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        return (adService.removeAd(adId)) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    
    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Long adId, @RequestBody CreateOrUpdateAd dto) {
        log.info("Method: {}", LoggingMethodImpl.getMethodName());
        Ad ad = adService.updateAds(adId, dto);
        if (ad != null) {
            return ResponseEntity.ok(ad);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("Method {}", LoggingMethodImpl.getMethodName());
        if (authentication.getName() != null) {
            String username = authentication.getName();
            return ResponseEntity.ok(adService.getAdsMe(username));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "hasRole('ADMIN') or @adServiceImpl.isAuthorAd(authentication.getName(), #adId)")
    public ResponseEntity<Void> updateImage(@PathVariable("id") Long adId,
                                            @RequestPart MultipartFile image,
                                            Authentication authentication) throws IOException {
        log.info("Method {}", LoggingMethodImpl.getMethodName());
        log.info("adId = {}", adId);
        adService.updateImage(adId, image);
        return ResponseEntity.ok().build();

    }
}