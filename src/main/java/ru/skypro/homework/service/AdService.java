package ru.skypro.homework.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

public interface AdService {

    Ad add(AdEntity adEntity);

    ExtendedAd getExtendedAd(Long id);

    Ad update(Long id, CreateOrUpdateAd updatedAd);

    void delete(Long id);

    Ads getAds();

    Ad createAdWithImage(CreateOrUpdateAd updatedAd, MultipartFile adFile);

    Ads getAdsMe();

}
