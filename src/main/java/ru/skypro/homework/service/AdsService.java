package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;

public interface AdsService {
    AdDto addAd(AdDto adDTO, Authentication authentication);
    AdDto getAd(Integer id);
    AdDto updateAd(Integer id, CreateOrUpdateAd adDto);
    boolean deleteAd(Integer id,  Authentication authentication);
    Ads getAllAds();
    AdDto getAdById(Integer id);
    Ads getAdsByUserId(Authentication authentication);
    AdDto updateImageAd(Integer id, MultipartFile image);
}
