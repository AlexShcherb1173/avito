package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.generatedDto.AdDto;
import ru.skypro.homework.dto.generatedDto.AdsDto;
import ru.skypro.homework.dto.generatedDto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.generatedDto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

public interface AdService {

    AdDto createAd(CreateOrUpdateAdDto createAd, MultipartFile image, Authentication authentication);

    Ad getAdById(long id);

    void removeAd(long id, Authentication authentication);

    AdsDto getAllAds();

    ExtendedAdDto getFullAdById(long id);

    AdDto updateAdById(long id, CreateOrUpdateAdDto createOrUpdateAdDto, Authentication authentication);

    AdsDto getAllAdsForUser(String username);
}
