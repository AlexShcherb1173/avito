package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Advertisements;
import ru.skypro.homework.dto.ads.AdvertisementsDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdvert;
import ru.skypro.homework.dto.ads.ExtendAdvert;

import java.io.IOException;

public interface AdService {
    Advertisements getAllAds();

    ExtendAdvert getAd(Integer adId);

    Advertisements getAdsMe(Authentication authentication);

    AdvertisementsDTO addAd(CreateOrUpdateAdvert properties, MultipartFile image, Authentication authentication) throws IOException;

    AdvertisementsDTO updateAd(Integer adId, CreateOrUpdateAdvert properties);

    String updateImage(Integer adId, MultipartFile image) throws IOException;

    void delete(Integer adId);
}
