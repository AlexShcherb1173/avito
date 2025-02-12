package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;
import java.util.List;

public interface AdService {

//    List<Ad> getAllAd();

    Ad createAd(CreateOrUpdateAd createAd, MultipartFile image, String userId) throws IOException;

    Ads getAdsByUser(String name);

    Ads getAllAds();

    ExtendedAd getAd(long id);

    boolean removeAd(long id);

    Ad updateAd(long id, CreateOrUpdateAd dto);


//    Ad updateAd(int id, CreateOrUpdateAd adDto);
//
//    void deleteAd(int id);
//
//    ExtendedAd getAdById(Integer id);
//
//    List<Ad> getMyAds();
}
