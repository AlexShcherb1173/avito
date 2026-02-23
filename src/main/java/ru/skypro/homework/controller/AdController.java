package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok(adService.getAdsMe());
    }

    @Operation(summary = "Добавление объявления")
    @ApiResponse(responseCode = "201", description = "Created")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.status(201).body(adService.addAd(properties, image));
    }

    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable int id) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(
            @PathVariable int id,
            @RequestBody CreateOrUpdateAd properties
    ) {
        return ResponseEntity.ok(adService.updateAd(id, properties));
    }

    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(
            @PathVariable int id,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(adService.updateImage(id, image));
    }
}