package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Advertisement.AdDTO;
import ru.skypro.homework.dto.Advertisement.Ads;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Advertisement.ExtendedAd;
import ru.skypro.homework.service.Mapper.AdMapper;
import ru.skypro.homework.service.impl.AdvertisementService;
import ru.skypro.homework.service.impl.ImageService;
import org.springframework.security.core.Authentication;
import java.io.IOException;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления",description = "Операции с объявлениями")
public class AdsController {

    private AdvertisementService advertisementService;
    private ImageService imageService;
    private AdMapper adMapper;
    private Authentication authentication;


    @Operation(summary = "Получение всех объявлений",tags = "Объявления")
    @GetMapping
    public Ads getAllAds() {
        return advertisementService.getAllAds();
    }

    @Operation(summary = "Добавление нового объявления")
    @SecurityRequirement(name = "basicAuth")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDTO addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                       @RequestPart("image") MultipartFile image) throws IOException {
        return advertisementService.createAd(properties, image, authentication.getName());
    }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable Long id) {
        return advertisementService.getExtendedAd(id);
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @advertisementService.isAuthor(#id, authentication.name)")
    public void deleteAd(
            @PathVariable Long id,
            Authentication authentication) throws IOException {
        advertisementService.deleteAd(id, authentication.getName());
    }

    @Operation(summary = "Обновление объявления")
    @PatchMapping("/{id}")
    public AdDTO updateAd(@PathVariable Long id,
                          @RequestBody CreateOrUpdateAd updatedAd,
                          Authentication authentication) {
        return advertisementService.updateAd(id, updatedAd, authentication.getName());
    }

    @Operation(summary = "Получение объявлений текущего пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return advertisementService.getAdsMe(authentication.getName());
    }

    @Operation(summary = "Обновление изображения объявления")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public byte[] updateAdImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) throws IOException {
        String imagePath = advertisementService.updateAdImage(id, image, authentication.getName());
        return imageService.loadImage(imagePath);
    }
}
