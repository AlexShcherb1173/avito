package ru.avito.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.AdsResponse;
import ru.avito.dto.ad.CreateOrUpdateAdRequest;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.dto.ad.ImageResponse;
import ru.avito.service.AdService;

import javax.validation.Valid;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    /**
     * Получить все объявления.
     */
    @GetMapping
    public ResponseEntity<AdsResponse> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    /**
     * Получить объявление по id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAdById(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }

    /**
     * Создать объявление с изображением.
     * Фронт отправляет multipart/form-data:
     * - image
     * - properties
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> createAd(
            @RequestPart("properties") @Valid CreateOrUpdateAdRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.ok(adService.createAd(request, image));
    }

    /**
     * Обновить объявление.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateAdRequest request
    ) {
        return ResponseEntity.ok(adService.updateAd(id, request));
    }

    /**
     * Удалить объявление.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить объявления текущего пользователя.
     */
    @GetMapping("/me")
    public ResponseEntity<AdsResponse> getMyAds() {
        return ResponseEntity.ok(adService.getMyAds());
    }

    /**
     * Обновить изображение объявления.
     */
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> updateAdImage(
            @PathVariable Integer id,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.ok(adService.updateAdImage(id, image));
    }
}