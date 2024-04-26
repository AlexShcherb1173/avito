package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.rs.ad.Ads;
import ru.skypro.homework.dto.rq.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.rs.ad.Ad;
import ru.skypro.homework.dto.rs.ad.ExtendedAd;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController { //advertisement контроллер - объявление контроллера
    /**
     * Добавление объявления {@code createAd}
     * @param properties свойства объявления
     * @param image медиа файл объявления
     * @return {@code ResponseEntity.ok(new Ad())} объявление добавилось
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createAd(@RequestPart(value = "properties", required = false) CreateOrUpdateAd properties,@RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(new Ad());
    }

    /**
     * Получение всех объявлений {@code getAds}
     * @return {@code ResponseEntity.ok(new Ads())} все объявления
     */
    @GetMapping
    public ResponseEntity<?> getAds() {
        return ResponseEntity.ok(new Ads());
    }

    /**
     * Получение информации об объявлении {@code getAdById}
     * @param id объявления
     * @return {@code ResponseEntity.ok(new ExtendedAd())} информация об объявлении
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(new ExtendedAd());
    }

    /**
     * Удаление объявления {@code deleteById}
     * @param id объявления
     * @return {@code ResponseEntity.ok().build()} код ответа сервера {@code №200, №401, №403, №404}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление информации об объявлении {@code getAdById}
     * @param id объявления
     * @param createOrUpdateAd измененные свойства объявления
     * @return {@code ResponseEntity.ok(new Ad())} обновленное объявление
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok(new Ad());
    }

    /**
     * Получение объявлений авторизованного пользователя {@code getMe}
     * @return {@code ResponseEntity.ok(new Ads())} количество объявлений пользователя, все объявления пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<Ads> getMe() {
        return ResponseEntity.ok(new Ads());
    }

    /**
     * Обновление картинки объявления {@code getAdById}
     * @param adId объявления
     * @param image картинки объявление пользователем
     * @return {@code ResponseEntity.ok(new User())} обновленное изображение объявления
     */
    @PatchMapping(value ="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("id") Integer adId, @RequestPart MultipartFile image) {
        return ResponseEntity.ok().build();
    }

}