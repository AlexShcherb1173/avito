package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {

    /**
     * получение всех объявлений
     */
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads emptyAds = new Ads();
        return ResponseEntity.ok(emptyAds);
    }

    /**
     * Добавление нового объявления
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                                    @RequestPart("image") MultipartFile image) {
        Ad emptyAd = new Ad();
        return ResponseEntity.ok(emptyAd);
    }

    /**
     * Получение информации об объявлении
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        ExtendedAd emptyExtendedAd = new ExtendedAd();
        return ResponseEntity.ok(emptyExtendedAd);
    }

    /**
     * Удаление объявления
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление объявления
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd updatedAd) {
        Ad emptyAd = new Ad();
        return ResponseEntity.ok(emptyAd);
    }

    /**
     * Получение объявлений текущего пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        Ads emptyAds = new Ads();
        return ResponseEntity.ok(emptyAds);
    }

}
