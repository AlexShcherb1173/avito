package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private static final Logger log = LoggerFactory.getLogger(AdsController.class);

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info(" Получить все объявления по названию ");
        Ads ads = new Ads();
        ads.setCount(0);
        return ResponseEntity.ok(ads);
    }

    @PostMapping
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image) {
        log.info(" Добавить все вызванные ");
        Ad ad = new Ad();
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        log.info(" Получить все объявления по id" + id);
        ExtendedAd extendedAd = new ExtendedAd();
        return ResponseEntity.ok(extendedAd);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id){
        log.info(" Удалить объявления " + id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd){
        log.info(" Обновить объявления " + id);
        Ad ad = new Ad();
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(){
        log.info("Получить мои объявления ");
        Ads ads = new Ads();
        ads.setCount(0);
        return ResponseEntity.ok(ads);
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id, @RequestParam ("image") MultipartFile image){
        log.info(" Обновить изображение " + id);
        return ResponseEntity.ok().build();
    }
}
