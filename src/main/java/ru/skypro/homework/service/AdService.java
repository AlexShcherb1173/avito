package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

public interface AdService {
    Ads getAllAds();
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image) throws IOException;
    ExtendedAd getAd(Integer id);
    void removeAd(Integer id);
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd);
    Ads getAdsMe();
    byte[] updateAdImage(Integer id, MultipartFile image) throws IOException;
}