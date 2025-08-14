package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.*;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class AdsController {

    private final AdsService adsService;

    @GetMapping
    @Operation(summary = "Получить все объявления", responses = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить объявление", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<AdDTO> addAd(
            @RequestPart CreateOrUpdateAd properties,
            @RequestPart(required = false) MultipartFile image,
            Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adsService.addAd(properties, image, authentication.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию об объявлении", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getExtendedAd(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление", responses = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Void> removeAd(@PathVariable Integer id, Authentication authentication) {
        adsService.removeAd(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить информацию об объявлении", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateAd updateAd,
            Authentication authentication) {
        return ResponseEntity.ok(adsService.updateAd(id, updateAd, authentication));
    }

    @GetMapping("/me")
    @Operation(summary = "Получить объявления текущего пользователя", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getUserAds(authentication.getName()));
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновить изображение объявления", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<byte[]> updateAdImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) throws IOException {
        return ResponseEntity.ok(adsService.updateAdImage(id, image, authentication.getName()));
    }
}