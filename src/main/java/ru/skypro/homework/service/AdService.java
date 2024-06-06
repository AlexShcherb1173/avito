package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;

import java.io.IOException;
import java.util.List;

public interface AdService {
    AdsDto getAllAds();

    AdDto addAd(MultipartFile image, CreateOrUpdateAdDto properties) throws IOException;

    ExtendedAdDto getAd(int id);

    boolean removeAd(int id) throws IOException;

    AdDto updateAds(int id, CreateOrUpdateAdDto createOrUpdateAdDto);

    AdsDto getAdsMe();

    byte[] updateImage(int id, MultipartFile image) throws IOException;

    boolean hasAdAction(int adId);
}
