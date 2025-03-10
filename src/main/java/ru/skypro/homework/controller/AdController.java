package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.service.AdService;


/**
 * Контроллер для управления объявлениями.
 * Этот класс предоставляет REST API для работы с объявлениями, включая их создание, удаление, обновление и просмотр.
 * Доступ к определенным методам ограничен ролями и авторством объявления, используя аннотацию {@link PreAuthorize}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Методы для работы с объявлениями")
public class AdController {

    private final AdService adService;

    /**
     * Получает все объявления.
     *
     * @return {@link ResponseEntity} с объектом {@link AdsDTO}, содержащим список объявлений.
     */
    @GetMapping
    @Operation(summary = "Получить все объявления")
    public ResponseEntity<AdsDTO> getAllAds() {
        AdsDTO ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Добавляет новое объявление.
     *
     * @param properties данные объявления.
     * @param image      изображение объявления.
     * @return {@link ResponseEntity} с объектом {@link AdDTO}, содержащим данные созданного объявления.
     */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Добавить объявление")
    public ResponseEntity<AdDTO> addAd(@Parameter(description = "Данные объявления", required = true, content = @Content(schema =
    @Schema(implementation = CreateOrUpdateAdDTO.class)))
            @RequestPart("properties") CreateOrUpdateAdDTO properties,
            @Parameter(description = "Изображение объявления", required = true, content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE))
            @RequestPart("image") MultipartFile image
    ) {
        AdDTO ad = adService.addAd(properties, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(ad);

    }

    /**
     * Получает объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link AdDTO}, содержащим данные объявления.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить объявление")
    public ResponseEntity<ExtendedAdDTO> getAd(@PathVariable Integer id) {
        ExtendedAdDTO ad = adService.getAdById(id);
        return ResponseEntity.ok(ad);
    }

    /**
     * Удаляет объявление (доступно только администратору или автору объявления).
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} без содержимого при успешном удалении.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить объявление")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdAuthor(#id, @userServiceImpl.getCurrentUserEntity().getId())")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновляет данные объявления (доступно только администратору или автору объявления).
     *
     * @param id       идентификатор объявления.
     * @param updateAd данные для обновления объявления.
     * @return {@link ResponseEntity} с объектом {@link AdDTO}, содержащим обновленные данные объявления.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновить объявление")
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdAuthor(#id, @userServiceImpl.getCurrentUserEntity().getId())")
    public ResponseEntity<AdDTO> updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO updateAd) {
        AdDTO updatedAd = adService.updateAd(id, updateAd);
        return ResponseEntity.ok(updatedAd);
    }

    /**
     * Получает объявления текущего пользователя.
     *
     * @return {@link ResponseEntity} с объектом {@link AdsDTO}, содержащим список объявлений текущего пользователя.
     */
    @GetMapping("/me")
    @Operation(summary = "Получить мои объявления")
    public ResponseEntity<AdsDTO> getMyAds() {
        AdsDTO ads = adService.getMyAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение объявления.
     * @return {@link ResponseEntity} с пустым телом при успешном обновлении.
     */
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @Operation(summary = "Обновить изображение")
    public ResponseEntity<Void> updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) {
        adService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }
}

