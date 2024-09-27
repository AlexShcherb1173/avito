package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdExtendedDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

import java.security.Principal;

public interface AdService {
    /**
     * Получение всех объявлений
     * @return = получаем лист объявлений
     */
    AdsDto getAllAds();

    /**
     * Добавляем новое объявление
     * @param createOrUpdateAdDto = экземпляр CreateOrUpdateAdDto
     * @param image = путь к файлу с изображением
     * @param principal принципал, чье имя используется для идентификации автора
     * @return = экземпляр AdDto
     */
    AdDto addAd(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile image, Principal principal);

    /**
     * Получаем объявление по id
     * @param id = id объявления
     * @return = возвращает AdExtendedDto
     */
    AdExtendedDto getAdExtended(int id);

    /**
     * Удаляем объявление по id
     * @param id = id объявления
     */
    void DeleteAd(int id);

    /**
     * Обновление объявления
     * @param id = id объявления
     * @return = AdDto
     */
    AdDto updateAd(int id, CreateOrUpdateAdDto createOrUpdateAdDto);

    /**
     * Получение объявлений авторизованного пользователя
     * @param principal принципал, чье имя используется для идентификации автора
     */
    AdsDto getMyAds(Principal principal);

    /**
     * Обновление картинки объявления
     * @param id = id объявления
     * @param image = путь к file картинки
     */

    void patchImage(int id, MultipartFile image);
}
