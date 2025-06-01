package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.service.AdsService;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @Autowired
    private AdsService adsService; // Зависимость от сервиса

    // Получение всех объявлений
    @GetMapping
    public ResponseEntity<List<Ad>> getAllAds() {
        List<Ad> ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    // Добавление объявления
    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestBody Ad ad) {
        Ad newAd = adsService.addAd(ad);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAd);
    }

    // Получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long id) {
        Ad ad = adsService.getAdById(id);
        return ad != null ? ResponseEntity.ok(ad) : ResponseEntity.notFound().build();
    }

    // Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        boolean isDeleted = adsService.deleteAd(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long id,
                                       @RequestBody Ad updatedAd) {
        Ad ad = adsService.updateAd(id, updatedAd);
        return ad != null ? ResponseEntity.ok(ad) : ResponseEntity.notFound().build();
    }

    // Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getMyAds() {
        List<Ad> myAds = adsService.getMyAds(); // !Реализуйте логику для получения объявлений текущего пользователя
        return ResponseEntity.ok(myAds);
    }

    // Обновление картинки объявления
    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> updateAdImage(@PathVariable Long id,
                                              @RequestParam("image") MultipartFile image) {
        boolean isUpdated = adsService.updateAdImage(id, image);
        return isUpdated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
