package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;

public interface AdService {

    // Получение всех объявлений
    AdsDTO getAllAds();

    // Добавление объявления. AddAd required: image, properties
    AdDTO addAd(MultipartFile image, CreateOrUpdateAdDTO properties, String email);

    // Получение информации об объявлении, getAds, параметр - id
    ExtendedAdDTO getAds(int id);

    // Удаление объявления, removeAd, параметр - id
    void removeAd(int id);

    // Обновление информации об объявлении, updateAds, параметр - id, requestBody - CreateOrUpdateAd
    AdDTO updateAds(int id, CreateOrUpdateAdDTO properties);

    // Получение объявлений авторизованного пользователя, getAdsMe.
    // Проверка авторизации пользователя по логину (email), иначе - 401 ошибка
    AdsDTO getAdsMe();

    // Обновление картинки объявления updateImage, параметр - id,
    // requestBody - multipart required image, type - String
    String [] updateImage (int id, MultipartFile image);






}
