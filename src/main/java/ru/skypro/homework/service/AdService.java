package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.ImageEntity;

import java.io.IOException;

public interface AdService {
    AdsDto getAllAds();

    AdDto addAd(CreateOrUpdateAdDto properties, MultipartFile file) throws IOException;

    ExtendedAdDto getAds(Integer id);

    void removeAd(Integer id);

    AdsDto getAdsMe();

    AdDto updateDto(Integer id, CreateOrUpdateAdDto properties);

    void updateAdImage(Integer id, MultipartFile file) throws IOException;

    ImageEntity getAdImage(Integer adId);

    boolean hasAdAccess(Integer id);
}
