package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;

public interface AdsService {
    Ads getAllAds();
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, String username);
    ExtendedAd getExtendedAd(Integer id);
    void removeAd(Integer id, String username);
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd, String username);
    Ads getAdsByUser(String username);
    void updateAdImage(Integer id, MultipartFile image, String username);
    AdEntity getAdEntity(Integer id);
}