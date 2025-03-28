package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.models.AdEntity;

import java.util.List;

public interface AdService {
    AdsDto getAllAds();

    AdDto createAd(CreateOrUpdateAdDto adDto, MultipartFile multipartFile, UserDetails details);

    ExtendedAdDto getAdById(int id);

    void deleteAd(int id);

    AdDto updateAd(int id, CreateOrUpdateAdDto dto);

    AdsDto getMyAds(UserDetails userDetails);
    void updateAdImage(int id, MultipartFile image);
}
