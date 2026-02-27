package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface AdService {

    Ads getAllAds();

    Ads getAdsMe();

    Ad addAd(CreateOrUpdateAd properties, MultipartFile image);

    ExtendedAd getAdById(Long id);

    void deleteAd(Long id);

    Ad updateAd(Long id, CreateOrUpdateAd properties);

    byte[] updateImage(Long id, MultipartFile image);
}