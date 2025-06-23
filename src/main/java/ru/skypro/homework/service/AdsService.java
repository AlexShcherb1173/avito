package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Image;

import java.util.Collection;

public interface AdsService {
    AdDto addAd(AdDto adDTO, Authentication authentication);
    AdDto getAd(Integer id);
    AdDto updateAd(Integer id, CreateOrUpdateAd adDto);
    void deleteAd(Integer id);
    Ads getAllAds();
    AdDto getAdById(Integer id);
    Collection<AdDto> getAdsByUserId();
    AdDto updateImageAd(Integer id, MultipartFile image);
}
