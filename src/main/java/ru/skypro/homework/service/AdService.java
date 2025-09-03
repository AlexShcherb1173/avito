package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdService {
    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd properties, byte[] image);

    ExtendedAd getAd(Integer id);

    void removeAd(Integer id);

    Ad updateAd(Integer id, CreateOrUpdateAd updateAd);

    Ads getAdsMe();

    void updateAdImage(Integer id, byte[] image);
}