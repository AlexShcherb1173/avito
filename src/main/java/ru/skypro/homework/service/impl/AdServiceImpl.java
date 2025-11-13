package ru.skypro.homework.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

@Service
public class AdServiceImpl implements AdService {
    @Override
    public AdsDto getAllAds() {
        return null;
    }

    @Override
    public ExtendedAdDto getAd(Integer id) {
        return null;
    }

    @Override
    public AdsDto getMyAds(String username) {
        return null;
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdDto createOrUpdateAdDto, String userName, MultipartFile image) throws IOException {
        return null;
    }

    @Override
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String username) {
        return null;
    }

    @Override
    public void deleteAd(Integer id, String username) {

    }

    @Override
    public byte[] getAdImage(Integer id) throws IOException {
        return new byte[0];
    }

    @Override
    public String getAdImageImageContentType(Integer id) throws IOException {
        return "";
    }

    @Override
    public boolean updateAdImage(Integer id, MultipartFile image, String username) throws IOException {
        return false;
    }
}
