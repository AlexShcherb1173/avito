package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

/**
 * Сервисный интерфейс для работы с объявлениями.
 *
 * Интерфейс предоставляет методы для выполнения операций с объявлениями:
 * - Преобразование сущности {@link Ad} в {@link AdDTO} {@link #getAdDTO(Ad)}.
 * - Получение объявления по ID {@link #getAdById(Integer)}.
 * - Добавление нового объявления {@link #addAd(CreateOrUpdateAdDTO, MultipartFile)}.
 * - Получение всех объявлений {@link #getAllAds()}.
 * - Обновление существующего объявления {@link #updateAd(Integer, CreateOrUpdateAdDTO)}.
 * - Получение объявлений пользователя {@link #getMyAds()}.
 * - Создание сущности {@link Ad} на основе данных {@link AdDTO} {@link #createAdFromDTO(AdDTO)}.
 * - Проверка, является ли пользователь автором объявления {@link #isAdAuthor(Integer, Integer)}.
 * - Удаление объявления {@link #deleteAd(Integer)}.
 * - Обновление изображения объявления {@link #updateAdImage(Integer, MultipartFile)}.
 *
 * Методы интерфейса возвращают объекты DTO, такие как {@link AdDTO} и {@link AdsDTO}, а также выполняют операции обновления и получения данных.
 */
public interface AdService {

    AdDTO getAdDTO(Ad ad);

    ExtendedAdDTO getAdById(Integer id);

    AdDTO addAd(CreateOrUpdateAdDTO adDTO, MultipartFile image);

    AdsDTO getAllAds();

    AdDTO updateAd(Integer id, CreateOrUpdateAdDTO updateAd);

    AdsDTO getMyAds();

    Ad createAdFromDTO(AdDTO adDTO);

    boolean isAdAuthor(Integer adId, Integer userId);

    void deleteAd(Integer id);

    void updateAdImage(Integer id, MultipartFile image);
}
