package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.Ads;

import java.io.IOException;
import java.util.List;

public interface AdsService {
    List<Ads> getAllAds();

    Ads saveAd(CreateOrUpdateAdDto createOrUpdateAdDto, String name, MultipartFile file);

    Ads getAd(Integer id);

    boolean removeAd(String name, Integer id);

    Ads updateAds(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String name);

    /**
     * Возвращает объявления авторизованного пользователя
     *
     * @param authentication данные о текущем пользователе
     * @return список объявлений
     */
    AdsDto getAllUserAds(Authentication authentication);

    /**
     * Обновляет картинку объявления
     *
     * @param id    идентификатор объявления
     * @param image новая картинка
     * @return добавленная картинка
     */
    boolean updateImageAd(int id, MultipartFile image, String email);
}
