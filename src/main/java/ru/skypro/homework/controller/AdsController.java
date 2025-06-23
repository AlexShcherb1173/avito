package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Advertisement.Ad;
import ru.skypro.homework.dto.Advertisement.Ads;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Advertisement.ExtendedAd;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления",description = "Операции с объявлениями")
public class AdsController {

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        return new Ads();
    }

    @Operation(summary = "Добавление нового объявления")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                    @RequestPart("image") MultipartFile image) {
        return new Ad();
    }


    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable Integer id) {
        return new ExtendedAd();
    }

    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление объявления")
    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable Integer id,
                       @RequestBody CreateOrUpdateAd updatedAd) {
        return new Ad();
    }

    @Operation(summary = "Получение объявлений текущего пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return new Ads();
    }
}
