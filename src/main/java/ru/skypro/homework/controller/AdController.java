package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.service.AdService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {

    private final AdService adService;

    @GetMapping
    @Operation(summary = "Получить все объявления")
    public ResponseEntity<AdsDTO> getAllAds() {
        AdsDTO ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Добавить объявление")
    public ResponseEntity<AdDTO> addAd(
            @RequestPart("properties") CreateOrUpdateAdDTO properties,
            @RequestPart("image") MultipartFile image
    ) {
        AdDTO ad = adService.addAd(properties, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить объявление")
    public ResponseEntity<AdDTO> getAd(@PathVariable Integer id) {
        AdDTO ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @PreAuthorize("hasRole('ADMIN') or @adService.isAdAuthor(#id, authentication.name)")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить объявление")
    @PreAuthorize("hasRole('ADMIN') or @adService.isAdAuthor(#id, authentication.name)")
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateAdDTO updateAd
    ) {
        AdDTO updatedAd = adService.updateAd(id, updateAd);
        return ResponseEntity.ok(updatedAd);
    }

    @GetMapping("/me")
    @Operation(summary = "Получить мои объявления")
    public ResponseEntity<AdsDTO> getMyAds() {
        AdsDTO ads = adService.getMyAds();
        return ResponseEntity.ok(ads);
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @Operation(summary = "Обновить изображение")
    public ResponseEntity<Void> updateAdImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image
    ) {
        adService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }
}
