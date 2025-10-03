package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.service.AdService;

/**
 * REST-контроллер для обработки HTTP-запросов, связанных с объявлениями.
 * Предоставляет API для операций CRUD над объявлениями.
 */
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    /**
     * Получает список всех объявлений.
     * Доступно всем пользователям, включая неавторизованных.
     *
     * @return ResponseEntity, содержащий {@link AdsDto} со списком объявлений
     */
    @GetMapping
    public AdsDto getAllAds() {
        return adService.getAllAds();
    }

    /**
     * Получает объявление по его идентификатору.
     *
     * @param id идентификатор запрашиваемого объявления
     * @return DTO объявления
     */
    @GetMapping("/{id}")
    public AdDto getAd(@PathVariable Integer id) {
        return adService.getAdById(id);
    }

    /**
     * Создает новое объявление.
     * Требует авторизации пользователя.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param ad DTO с данными для создания объявления
     * @return DTO созданного объявления
     */
    @PostMapping
    public AdDto createAd(Authentication authentication, @RequestBody CreateOrUpdateAdDto ad) {
        return adService.createAd(authentication, ad);
    }

    /**
     * Удаляет объявление по идентификатору.
     * Удалять может только автор объявления или администратор.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param id идентификатор объявления для удаления
     */
    @DeleteMapping("/{id}")
    public void deleteAd(Authentication authentication, @PathVariable Integer id) {
        adService.deleteAd(authentication, id);
    }

    /**
     * Обновляет существующее объявление.
     * Редактировать может только автор объявления или администратор.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @param id идентификатор объявления для обновления
     * @param ad DTO с обновленными данными объявления
     * @return DTO обновленного объявления
     */
    @PatchMapping("/{id}")
    public AdDto updateAd(Authentication authentication, @PathVariable Integer id, @RequestBody CreateOrUpdateAdDto ad) {
        return adService.updateAd(authentication, id, ad);
    }
}