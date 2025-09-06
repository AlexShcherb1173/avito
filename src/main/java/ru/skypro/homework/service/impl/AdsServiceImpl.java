package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;

import java.util.Collections;

@Service
public class AdsServiceImpl implements AdsService {
    @Override public Ads getAllAds() {
        Ads a = new Ads();
        a.setCount(0);
        a.setResults(Collections.emptyList());
        return a;
    }
    @Override public Ad addAd(CreateOrUpdateAd props, MultipartFile image) { return new Ad(); }
    @Override public Ads getMyAds() { return getAllAds(); }
    @Override public ExtendedAd getAd(int id) { return new ExtendedAd(); }
    @Override public void removeAd(int id) { }
    @Override public Ad updateAd(int id, CreateOrUpdateAd dto) { return new Ad(); }
    @Override public byte[] updateImage(int id, MultipartFile image) { return new byte[0]; }
}
