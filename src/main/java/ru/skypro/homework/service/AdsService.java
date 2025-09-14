package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface AdsService {
    /**
     * Возвращает список всех объявлений.
     * @return DTO-обёртка со счётчиком и списком объявлений
     */
    Ads getAllAds();

    /**
     * Создаёт объявление от имени текущего пользователя.
     * @param props  свойства объявления (title/price/description)
     * @param image  картинка (опционально на этапе III)
     * @return краткое DTO созданного объявления
     */
    Ad addAd(CreateOrUpdateAd props, MultipartFile image);

    /**
     * Возвращает объявления текущего пользователя.
     * @return DTO-обёртка со списком объявлений
     */
    Ads getMyAds();

    /**
     * Возвращает расширенную информацию по объявлению.
     * @param id идентификатор объявления
     * @return расширенное DTO объявления
     */
    ExtendedAd getAd(int id);

    /**
     * Удаляет объявление. Разрешено владельцу или администратору.
     * @param id идентификатор объявления
     */
    void removeAd(int id);

    /**
     * Обновляет данные объявления. Разрешено владельцу или администратору.
     * @param id   идентификатор объявления
     * @param dto  новые поля
     * @return обновлённое краткое DTO
     */
    Ad updateAd(int id, CreateOrUpdateAd dto);

    /**
     * Обновляет картинку объявления. Разрешено владельцу или администратору.
     * @param id    идентификатор объявления
     * @param image файл изображения
     * @return бинарные данные (по спецификации возвращается octet-stream)
     */
    byte[] updateImage(int id, MultipartFile image);
}
