package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.io.IOException;

public interface AdService {
    Ads getAllAds();
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;
    ExtendedAd getAd(Integer id);
    AdEntity getAdEntity(Integer id);
    void removeAd(Integer id, Authentication authentication);
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication);
    Ads getAdsMe(Authentication authentication);
    byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication) throws IOException;
    byte[] getAdImage(String filename) throws IOException;
}