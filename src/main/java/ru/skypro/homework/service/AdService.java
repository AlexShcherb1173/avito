package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.model.Ad;

import java.io.IOException;

/**
 * Интерефейс для сервиса объявлений.
 */
public interface AdService {
    void removeAdImage(String adImageUrl);

    AdListDto getAllAds();

    AdDto addAd(MultipartFile image, CreateOrUpdateAdDto ad);

    void saveAdImage(MultipartFile image, Ad ad);

    ExtendedAdDto getAdById(Integer id);

    AdListDto getUserAds();

    AdDto updateAd(Integer adId, CreateOrUpdateAdDto ad) throws ForbiddenException;

    void removeAd(Integer adId) throws EntityNotFoundException, ForbiddenException;

    byte[] updateAdImage(Integer adId, MultipartFile image) throws IOException, ForbiddenException;

    byte[] readAdImageFromFs(String adImageUrl) throws IOException;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean checkAuthorization(Ad ad);
}
