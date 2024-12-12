package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdsService;
@Service
public class AdsServiceImpl implements AdsService {
    @Override
    public Ads getAllAds() {
        return null;
    }

    @Override
    public Ads addAds(MultipartFile image, CreateOrUpdateAdDto ad) {
        return null;
    }


    @Override
    public Ads getUserAds() {
        return null;
    }

    public void removeAd(Integer id) {
        ;
    }

    @Override
    public ExtendedAdDto getAdById(Integer id) {
        return null;
    }

    @Override
    public Ads updateAdById(Integer id, CreateOrUpdateAdDto ad) {
        return null;
    }

    @Override
    public String updateImageAd(Integer id, CreateOrUpdateAdDto ad) {
        return null;
    }
}
