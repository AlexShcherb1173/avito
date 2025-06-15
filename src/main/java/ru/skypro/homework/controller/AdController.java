package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService; // Зависимость от сервиса

    // Получение всех объявлений
    @GetMapping
    public ResponseEntity<List<Ad>> getAllAds() {
        List<Ad> ads = adService.getAllAds().getResults();
        return ResponseEntity.ok(ads);
    }

    // Добавление объявления
    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestBody Ad ad) {
        Ad newAd = adService.addAd(ad);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAd);
    }

    // Получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getAdById(@PathVariable Long id) {
        ExtendedAd ad = adService.getAdById(id);
        return ad != null ? ResponseEntity.ok(ad) : ResponseEntity.notFound().build();
    }

    // Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        boolean isDeleted = adService.deleteAd(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long id,
                                       @RequestBody Ad updatedAd) {
        Ad ad = adService.updateAd(id, updatedAd);
        return ad != null ? ResponseEntity.ok(ad) : ResponseEntity.notFound().build();
    }

    // Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getMyAds() {
        List<Ad> myAds = adService.getMyAds(); // !Реализуйте логику для получения объявлений текущего пользователя
        return ResponseEntity.ok(myAds);
    }

    // Обновление картинки объявления
    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> updateAdImage(@PathVariable Long id,
                                              @RequestParam("image") MultipartFile image) {
        boolean isUpdated = adService.updateAdImage(id, image);
        return isUpdated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}