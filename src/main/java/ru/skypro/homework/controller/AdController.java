package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            @RequestPart("properties") String properties,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            CreateOrUpdateAd createOrUpdateAd =
                    objectMapper.readValue(properties, CreateOrUpdateAd.class);

            return ResponseEntity
                    .status(201)
                    .body(adService.addAd(createOrUpdateAd, image));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable Long id) {
        return ResponseEntity.ok(adService.getAdById(id));
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateAd properties
    ) {
        return ResponseEntity.ok(adService.updateAd(id, properties));
    }

    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(
            @PathVariable Long id,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(adService.updateImage(id, image));
    }
}