package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        AdsDto adsDto = adService.getAllAds();
        return ResponseEntity.ok(adsDto);
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDto> getMyAds(Authentication authentication) {
        String username = authentication.getName();
        AdsDto adsDto = adService.getMyAds(username);
        return ResponseEntity.ok(adsDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> addAd(@RequestParam("title") String title,
                                       @RequestParam("price") Integer price,
                                       @RequestParam("description") String description,
                                       @RequestPart("image") MultipartFile image,
                                       Authentication authentication) throws IOException {
        String username = authentication.getName();

        CreateOrUpdateAdDto properties = new CreateOrUpdateAdDto();
        properties.setTitle(title);
        properties.setPrice(price);
        properties.setDescription(description);

        try {
            AdDto adDto = adService.createAd(properties, username, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(adDto);
        } catch (IOException e) {
            log.error("Failed to create ad", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Operation(
            summary = "Получение информации об объявлении"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable Integer id) {
        ExtendedAdDto extendedAdDto = adService.getAd(id);
        return ResponseEntity.ok(extendedAdDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer adId, Authentication authentication) {
        adService.deleteAd(adId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Integer adId,
                                           @RequestBody CreateOrUpdateAdDto createOrUpdateAdDto,
                                           Authentication authentication) {
        String username = authentication.getName();
        AdDto updateAd = adService.updateAd(adId, createOrUpdateAdDto, username);
        return ResponseEntity.ok(updateAd);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) throws IOException {
        byte[] image = adService.getAdImage(id);
        String contentType = adService.getAdImageContentType(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(image);
    }

    //обновление картинки объявления
    @Operation(
            summary = "Обновление картинки объявления",
            description = "Загружает новое изображение для указанного объявления"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDto> updateImage(@PathVariable("id") Integer id,
                                             @RequestParam("image") MultipartFile image,
                                             Authentication authentication) throws IOException {
        String username = authentication.getName();
        AdDto updatedAd = adService.updateAdImage(id, image, username);

        return ResponseEntity.ok(updatedAd);
    }
}
