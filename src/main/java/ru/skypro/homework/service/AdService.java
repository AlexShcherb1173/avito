package ru.skypro.homework.service;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdService {
    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd properties, byte[] image, Authentication authentication);

    ExtendedAd getAd(Integer id);

    void removeAd(Integer id);

    Ad updateAd(Integer id, CreateOrUpdateAd updateAd);

    Ads getAdsMe(Authentication authentication);

}