package ru.skypro.homework.service;

import ru.skypro.homework.dto.CreateOrUpdateAd;

public interface AdService {
    boolean getAllAds();
    boolean createAd(Object properties, String image);
    boolean getAdInfo(int id);
    boolean deleteAd(int id);
    boolean updateAdInfo(int id, CreateOrUpdateAd ad);
    boolean getAdsOfUser();
    boolean updateAdImage(int id, String image);
}
