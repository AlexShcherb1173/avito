package ru.avito.service;

import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.AdsResponse;
import ru.avito.dto.ad.CreateOrUpdateAdRequest;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.dto.ad.ImageResponse;

public interface AdService {

    AdsResponse getAllAds();

    ExtendedAdDto getAdById(Integer id);

    AdDto createAd(CreateOrUpdateAdRequest request, MultipartFile image);

    AdDto updateAd(Integer id, CreateOrUpdateAdRequest request);

    void deleteAd(Integer id);

    AdsResponse getMyAds();

    ImageResponse updateAdImage(Integer id, MultipartFile image);
}