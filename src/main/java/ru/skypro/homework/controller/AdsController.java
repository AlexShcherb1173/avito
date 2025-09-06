package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {
    private final AdsService service;
    public AdsController(AdsService service) { this.service = service; }

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() { return service.getAllAds(); }

    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Ad addAd(@RequestPart("properties") @Valid CreateOrUpdateAd props,
                    @RequestPart("image") MultipartFile image) {
        return service.addAd(props, image);
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() { return service.getMyAds(); }

    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable int id) { return service.getAd(id); }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAd(@PathVariable int id) { service.removeAd(id); }

    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable int id, @RequestBody @Valid CreateOrUpdateAd dto) {
        return service.updateAd(id, dto);
    }

    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public byte[] updateImage(@PathVariable int id, @RequestPart("image") MultipartFile image) {
        return service.updateImage(id, image);
    }
}
