package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;

public interface AdsService {
    Ads getAllAds();
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image, String username);
    ExtendedAd getExtendedAd(Integer id);

    @PreAuthorize("hasRole('ADMIN') or @adsService.isAdOwner(#id, authentication.name)")
    void removeAd(Integer id, String username);

    @PreAuthorize("hasRole('ADMIN') or @adsService.isAdOwner(#id, authentication.name)")
    Ad updateAd(Integer id, CreateOrUpdateAd updateAd, String username);

    Ads getAdsByUser(String username);

    @PreAuthorize("hasRole('ADMIN') or @adsService.isAdOwner(#id, authentication.name)")
    void updateAdImage(Integer id, MultipartFile image, String username);

    AdEntity getAdEntity(Integer id);

    boolean isAdOwner(Integer adId, String username);
}