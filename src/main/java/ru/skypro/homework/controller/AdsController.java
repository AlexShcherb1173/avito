package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.build.Plugin;
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
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления")
// управление объявлениями:
public class AdsController {
    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(new Ads()); // Пустой список объявлений
    }


    @PostMapping(consumes = "multipart/form-data")
    @Operation (summary= "Добавления объявления")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties, // JSON данные
            @RequestPart("image") MultipartFile image) {            // Файл изображения
        return ResponseEntity.ok(new Ad());
    }


    @GetMapping("/{id}") // {id} - path variable (переменная пути)
    @Operation (summary= "Получение информации об объявлении")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable("id") Integer id) {
        // @PathVariable - извлекаем id из URL
        return ResponseEntity.ok(new ExtendedAd());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    public ResponseEntity<?> removeAd(@PathVariable("id") Integer id) {
        return ResponseEntity.noContent().build(); // 204 No Content при успешном удалении
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Integer id,
                                        @RequestBody CreateOrUpdateAd updateAd) {
        return ResponseEntity.ok(new Ad());
    }
    @GetMapping("/me") // GET /ads/me - объявления текущего пользователя
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public ResponseEntity<Ads> getAdsMe() {
        return ResponseEntity.ok(new Ads());
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @Operation (summary = "Обновление картинки объявления")
    public ResponseEntity<byte[]> updateImage(@PathVariable("id") Integer id,
                                              @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(new byte[0]); // Пока возвращаем пустой массив байтов
    }
}
