package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        // Заглушка для получения всех объявлений
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) {
        // Заглушка для добавления объявления
        AdDTO ad = new AdDTO();
        ad.setPk(1);
        ad.setTitle(properties.getTitle());
        ad.setPrice(properties.getPrice());
        ad.setAuthor(1);
        ad.setImage("/images/ads/1.jpg");
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        // Заглушка для получения объявления
        ExtendedAd ad = new ExtendedAd();
        ad.setPk(id);
        ad.setTitle("Пример объявления");
        ad.setPrice(1000);
        ad.setDescription("Пример описания");
        ad.setAuthorFirstName("Иван");
        ad.setAuthorLastName("Иванов");
        ad.setEmail("user@example.com");
        ad.setPhone("+7(123)456-78-90");
        ad.setImage("/images/ads/1.jpg");
        return ResponseEntity.ok(ad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        // Заглушка для удаления объявления
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateAd updateAd) {
        // Заглушка для обновления объявления
        AdDTO ad = new AdDTO();
        ad.setPk(id);
        ad.setTitle(updateAd.getTitle());
        ad.setPrice(updateAd.getPrice());
        ad.setAuthor(1);
        ad.setImage("/images/ads/" + id + ".jpg");
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/my")
    public ResponseEntity<Ads> getMyAds() {
        // Заглушка для получения объявлений пользователя
        Ads ads = new Ads();
        ads.setCount(0);
        ads.setResults(Collections.emptyList());
        return ResponseEntity.ok(ads);
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateAdImage(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image) {
        // Заглушка для обновления изображения объявления
        try {
            return ResponseEntity.ok(image.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
