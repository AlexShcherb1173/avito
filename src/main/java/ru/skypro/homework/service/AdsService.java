package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdsService {
    public Ads getAllAds();

    public Ads addAds(MultipartFile image, CreateOrUpdateAd ad);

    public Ads getUserAds();

    public void removeAd(Integer id);

    ExtendedAd getAdById(Integer id);

    Ads updateAdById(Integer id, CreateOrUpdateAd ad);

    String updateImageAd(Integer id, CreateOrUpdateAd ad);
}


