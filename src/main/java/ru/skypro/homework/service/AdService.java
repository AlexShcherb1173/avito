package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;

import java.io.IOException;

public interface AdService {
    AdsDto getAllAds();

    ExtendedAdDto getAd(Integer id);

    AdsDto getMyAds(String username);

    AdDto createAd(CreateOrUpdateAdDto createOrUpdateAdDto, String username, MultipartFile image) throws IOException;

    void deleteAd(Integer id, String username);

    AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String username);

    byte[] getAdImage(Integer id) throws IOException;

    String getAdImageContentType(Integer id) throws IOException;

    AdDto updateAdImage(Integer id, MultipartFile image, String username) throws IOException;

}
