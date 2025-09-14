package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

/**
 * Контроллер для работы с объявлениями.
 * Обрабатывает HTTP-запросы, связанные с созданием, получением, редактированием и удалением объявлений.
 */
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService service;

    /**
     * Конструктор контроллера.
     * @param service сервис для работы с объявлениями
     */
    public AdsController(AdsService service) {
        this.service = service;
    }

    /**
     * Получение списка всех объявлений.
     * @return список объявлений с общим количеством
     */
    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public Ads getAllAds() {
        return service.getAllAds();
    }

    /**
     * Добавление нового объявления.
     * @param props DTO с параметрами объявления
     * @param image изображение объявления
     * @return созданное объявление
     */
    @Operation(summary = "Добавление объявления")
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public Ad addAd(@RequestPart("properties") @Valid CreateOrUpdateAd props,
                    @RequestPart("image") MultipartFile image) {
        return service.addAd(props, image);
    }

    /**
     * Получение списка объявлений текущего авторизованного пользователя.
     * @return список его объявлений
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return service.getMyAds();
    }

    /**
     * Получение расширенной информации об объявлении по ID.
     * @param id ID объявления
     * @return расширенная информация об объявлении
     */
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    public ExtendedAd getAd(@PathVariable int id) {
        return service.getAd(id);
    }

    /**
     * Удаление объявления по ID.
     * @param id ID объявления
     */
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAd(@PathVariable int id) {
        service.removeAd(id);
    }

    /**
     * Обновление информации об объявлении.
     * @param id ID объявления
     * @param dto DTO с новыми данными
     * @return обновлённое объявление
     */
    @Operation(summary = "Обновление информации об объявлении")
    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable int id, @RequestBody @Valid CreateOrUpdateAd dto) {
        return service.updateAd(id, dto);
    }

    /**
     * Обновление изображения объявления.
     * @param id ID объявления
     * @param image новое изображение
     * @return массив байтов изображения
     */
    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public byte[] updateImage(@PathVariable int id, @RequestPart("image") MultipartFile image) {
        return service.updateImage(id, image);
    }
}
