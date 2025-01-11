package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

public interface AdsService {
    Ads getAllAds();

    Ads addAds(MultipartFile image, CreateOrUpdateAdDto ad, Authentication authentication);

    Ads getUserAds(Authentication authentication);

    void removeAd(Integer id, Authentication authentication);

    ExtendedAdDto getAdById(Integer id);

    Ads updateAdById(Integer id, CreateOrUpdateAdDto ad, Authentication authentication);

    String updateImageAd(Integer id, MultipartFile image, Authentication authentication);
}