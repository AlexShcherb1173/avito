package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.io.IOException;

public interface AdsService {
    Ads getAllAds();

    void addAds(MultipartFile image, CreateOrUpdateAdDto ad) throws IOException;

    Ads getUserAds();

    void removeAd(Integer id);

    ExtendedAdDto getAdById(Integer id);

    void updateAdById(Integer id, CreateOrUpdateAdDto ad);

    void updateImageAd(Integer id, MultipartFile image) throws IOException;
}