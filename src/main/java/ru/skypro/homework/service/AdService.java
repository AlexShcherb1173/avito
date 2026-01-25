package ru.skypro.homework.service;

import ru.skypro.homework.model.Ad;

import java.util.List;

public interface AdService {

    List<Ad> getAllAds();

    Ad getAd(Integer id);
}
