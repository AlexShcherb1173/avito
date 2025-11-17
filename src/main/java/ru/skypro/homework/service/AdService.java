package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

import java.io.IOException;

@Service
public interface AdService {
    AdsDto getAllAds();

    AdDto addAd(CreateOrUpdateAdDto properties, MultipartFile image) throws IOException;

    ExtendedAdDto getAdById(Integer id);

    void removeAd(Integer id);

    AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto);

    AdsDto getAllMyAds();

    byte[] uploadAdPicture(Integer id, MultipartFile file) throws IOException;

    Ad findAdById(Integer id);
}
