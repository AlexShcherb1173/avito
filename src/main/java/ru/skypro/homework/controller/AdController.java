package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

@Slf4j
@RestController
@RequestMapping("/ads")
public class AdController {

    //получение всех объявлений
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        Ads ads = new Ads();
        return ResponseEntity.ok(ads);
    }

    //добавление объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@PathVariable("createOrUpdateAd") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestParam("image") MultipartFile image) {
        Ad ad = new Ad();
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        if (true) {
            return ResponseEntity.ok(ad);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable Integer id) {
        ExtendedAd extendedAd = new ExtendedAd();
        if (id == null) {
            return ResponseEntity.notFound().build();   //404
        }
        if (true) {
            return ResponseEntity.ok(extendedAd);       //200
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  //401
        }
    }

    //удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer adId) {
        return ResponseEntity.noContent().build();
    }

    //обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Integer adId,
                                        @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        Ad ad = new Ad();
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        return ResponseEntity.ok(ad);
    }

    //получение объявлений авторизированного пользователя
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe() {
        Ads ads = new Ads();
        return ResponseEntity.ok(ads);
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
    public ResponseEntity<byte[]> updateImage(@PathVariable("adId") Integer adId,
                                              @RequestParam("image") MultipartFile image) {
        try {
            byte[] imageData = new byte[100];
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(imageData);
        } catch(RuntimeException e){
            log.error("Error updating image for ad: {}", adId, e);
            return ResponseEntity.notFound().build();   //404
        }
    }
}
