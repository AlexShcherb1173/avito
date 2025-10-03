package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.model.Ad;

/**
 * Контроллер для управления объявлениями.
 * Включает эндпоинты для создания, редактирования и удаления объявлений.
 */
@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;

    /**
     * Создает новое объявление.
     * Присваивает объявлению текущего авторизованного пользователя как автора.
     *
     * @param adDto объект, содержащий информацию о новом объявлении
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return созданное объявление
     */
    @PostMapping
    public Ad createAd(@RequestBody AdDto adDto, Authentication authentication) {
        Ad ad = new Ad();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());
        return adService.createAd(ad, authentication);
    }

    /**
     * Обновляет информацию об объявлении.
     * Пользователь может редактировать только свои объявления.
     *
     * @param id идентификатор объявления
     * @param adDto объект, содержащий обновленную информацию о объявлении
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @return обновленное объявление
     * @throws SecurityException если пользователь не авторизован редактировать это объявление
     */
    @PatchMapping("/{id}")
    public Ad updateAd(@PathVariable Integer id, @RequestBody AdDto adDto, Authentication authentication) {
        if (adService.canEditAd(id, authentication)) {
            Ad ad = new Ad();
            ad.setTitle(adDto.getTitle());
            ad.setPrice(adDto.getPrice());
            ad.setDescription(adDto.getDescription());
            return adService.createAd(ad, authentication);
        }
        throw new SecurityException("You are not authorized to edit this ad.");
    }

    /**
     * Удаляет объявление.
     * Пользователь может удалить только свои объявления.
     *
     * @param id идентификатор объявления
     * @param authentication объект аутентификации, содержащий данные о текущем пользователе
     * @throws SecurityException если пользователь не авторизован удалить это объявление
     */
    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id, Authentication authentication) {
        if (adService.canEditAd(id, authentication)) {
            adService.deleteAd(id);
        } else {
            throw new SecurityException("You are not authorized to delete this ad.");
        }
    }
}
