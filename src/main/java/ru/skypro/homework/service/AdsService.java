package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import java.io.IOException;

public interface AdsService {
    Ads getAllAds();
    AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile imageFile, String username) throws IOException;
    ExtendedAd getExtendedAd(Integer id);
    void removeAd(Integer id, Authentication authentication);
    AdDTO updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication);
    Ads getUserAds(String username);
    byte[] updateAdImage(Integer id, MultipartFile imageFile, String username) throws IOException;
}