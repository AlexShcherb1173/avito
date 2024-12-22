package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdListDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.exception.AdImageException;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
/**
 * Контроллер для работы с объявлениями.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);
    private final AdService adService;
    /**
     * Конструктор контроллера AdController.
     */
    public AdController(AdService adService) {
        this.adService = adService;
    }
    /**
     * Получение всех объявлений.
     */
    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    public AdListDto getAllAds() {
        return adService.getAllAds();
    }
    /**
     * Добавление объявления.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление объявления")
    public AdDto addAd(@RequestParam("image") MultipartFile image, @RequestPart("properties") CreateOrUpdateAdDto ad) {
        log.info("Добавление объявления {}", ad.getTitle());
        return adService.addAd(image, ad);
    }
    /**
     * Получение информации об объявлении.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    public ExtendedAdDto getAdById(@PathVariable Integer id) {
        ExtendedAdDto ad = new ExtendedAdDto();
        try {
            ad = adService.getAdById(id);
        } catch (AdImageException e) {
            log.warn(e.getMessage());
        }
        return ad;
    }
    /**
     * Обновление информации об объявлении.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении")
    public AdDto updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        log.info("Обновление информации об объявлении {}", ad.getTitle());
        return adService.updateAd(id, ad);
    }
    /**
     * Удаление объявления.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    public void removeAd(@PathVariable Integer id, Authentication auth) {
        log.info("Пользователь {} удаляет объявления {}", ((UserDetails) auth.getPrincipal()).getUsername(), id);
        adService.removeAd(id);
    }
    /**
     * Получение объявлений авторизованного пользователя.
     */
    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    public AdListDto getUserAds() {
        return adService.getUserAds();
    }
    /**
     * Обновление изображения объявления.
     */
    @PatchMapping(path = "/{adId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Обновление изображения объявления")
    public ResponseEntity<byte[]> updateAdImage(@PathVariable Integer adId, @RequestParam("image") MultipartFile image) throws IOException {
        log.info("Обновление изображения объявления {}", adId);
        return ResponseEntity.ok().body(adService.updateAdImage(adId, image));
    }
}