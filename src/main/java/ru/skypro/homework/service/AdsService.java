package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

public interface AdsService {
    public Ads getAllAds();

    public Ads addAds(MultipartFile image, CreateOrUpdateAdDto ad);

    public Ads getUserAds();

    public void removeAd(Integer id);

    ExtendedAdDto getAdById(Integer id);

    Ads updateAdById(Integer id, CreateOrUpdateAdDto ad);

    String updateImageAd(Integer id, CreateOrUpdateAdDto ad);
}


