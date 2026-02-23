package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdService {

    public Ads getAllAds() {

        Ad ad = new Ad();
        ad.setAuthor(1);
        ad.setImage("/images/ad1.jpg");
        ad.setPk(10);
        ad.setPrice(15000);
        ad.setTitle("Продам велосипед");

        List<Ad> adList = new ArrayList<>();
        adList.add(ad);

        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);

        return ads;
    }

    public Ads getAdsMe() {

        Ad ad = new Ad();
        ad.setAuthor(1);
        ad.setImage("/images/myAd.jpg");
        ad.setPk(200);
        ad.setPrice(25000);
        ad.setTitle("Мой велосипед");

        List<Ad> adList = new ArrayList<>();
        adList.add(ad);

        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);

        return ads;
    }

    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {

        Ad ad = new Ad();
        ad.setAuthor(1);
        ad.setImage("/images/newAd.jpg");
        ad.setPk(100);
        ad.setPrice(properties.getPrice());
        ad.setTitle(properties.getTitle());

        return ad;
    }

    public ExtendedAd getAdById(int id) {

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(id);
        extendedAd.setAuthorFirstName("Иван");
        extendedAd.setAuthorLastName("Иванов");
        extendedAd.setDescription("Отличный велосипед, почти новый");
        extendedAd.setEmail("user@gmail.com");
        extendedAd.setImage("/images/ad1.jpg");
        extendedAd.setPhone("+79991234567");
        extendedAd.setPrice(15000);
        extendedAd.setTitle("Продам велосипед");

        return extendedAd;
    }

    public void deleteAd(int id) {
        // пока заглушка
    }

    public Ad updateAd(int id, CreateOrUpdateAd properties) {

        Ad ad = new Ad();
        ad.setAuthor(1);
        ad.setImage("/images/updatedAd.jpg");
        ad.setPk(id);
        ad.setPrice(properties.getPrice());
        ad.setTitle(properties.getTitle());

        return ad;
    }

    public byte[] updateImage(int id, MultipartFile image) {
        return new byte[]{1, 2, 3};
    }
}