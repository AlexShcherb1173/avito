package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.List;
import java.util.Optional;

public interface AdService {

    List<Ad> getAllAd();

    Ad createAd(CreateOrUpdateAd adDto);

    ExtendedAd getAdById(int id);

    Ad updateAd(int id, CreateOrUpdateAd adDto);

    void deleteAd(int id);

    List<Ad> getMyAds();

    void updateImage(int id);
}
