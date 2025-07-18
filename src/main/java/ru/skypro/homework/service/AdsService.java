package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;

public interface AdsService {
    Ads getAllAds();
    AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile image);
    AdDTO getAds(int id);
    void removeAd (int id);
    AdDTO updateAds(int id, CreateOrUpdateAd updateAd);
    Ads getAdsMe();
    String updateImage(int id,MultipartFile image);

}