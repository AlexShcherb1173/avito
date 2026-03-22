package ru.avito.service;

import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.ad.*;

public interface AdService {

    AdsResponse getAllAds();

    ExtendedAdDto getAdById(Integer id);

    AdDto createAd(CreateOrUpdateAdRequest request);

    AdDto updateAd(Integer id, CreateOrUpdateAdRequest request);

    void deleteAd(Integer id);

    AdsResponse getMyAds();

    ImageResponse updateAdImage(Integer id, MultipartFile image);
}