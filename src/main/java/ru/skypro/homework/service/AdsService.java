package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface AdsService {
    // Получение всех объявлений
    Ads getAllAds();

    // Добавление объявления
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image) throws IOException;

    // Получение информации об объявлении
    ExtendedAd getExtendedAdById(long id) throws ObjectNotFoundException;

    // Удаление объявления
    void removeAd(long id) throws ForbiddenException, ObjectNotFoundException;

    // Обновление информации об объявлении
    Ad updateAds(long id, CreateOrUpdateAd createOrUpdateAd) throws ForbiddenException, ObjectNotFoundException;

    // Получение объявлений авторизованного пользователя
    Ads getAdsMe();

    // Обновление картинки объявления
    byte[] updateImage(long id, MultipartFile image) throws IOException, ObjectNotFoundException, ForbiddenException;
}
