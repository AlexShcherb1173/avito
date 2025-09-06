package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface AdsService {
    Ads getAllAds();
    Ad addAd(CreateOrUpdateAd props, MultipartFile image);
    Ads getMyAds();
    ExtendedAd getAd(int id);
    void removeAd(int id);
    Ad updateAd(int id, CreateOrUpdateAd dto);
    byte[] updateImage(int id, MultipartFile image);
}
