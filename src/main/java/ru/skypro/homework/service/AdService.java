package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.List;


public interface AdService {

    Ads getAllAds();

    Ad addAd(Ad ad);

    ExtendedAd getAd(Integer id);

    boolean deleteAd(Long id);

    Ad updateAd(Long id, Ad createOrUpdateAd);

    boolean updateAdImage(Long id, MultipartFile image);

    ExtendedAd getAdById(Long id);

    List<Ad> getMyAds();
}
