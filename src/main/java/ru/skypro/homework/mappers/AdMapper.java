package ru.skypro.homework.mappers;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class AdMapper {


    public Ads getAllAd(List<AdEntity> adsEntity){

        Ads ads = new Ads();
        int total = 0;
        List<Ad> adsList = new ArrayList<>();
        for (int i = 0; i < adsEntity.size(); i++) {
            Ad ad = new Ad();
            ad.setAuthor(adsEntity.get(i).getUser().getId());
            ad.setImage(adsEntity.get(i).getImageFileName());
            ad.setPk(adsEntity.get(i).getId());
            ad.setPrice(adsEntity.get(i).getPrice());
            ad.setTitle(adsEntity.get(i).getTitle());
            total++;
            ads.setCount(total);
            adsList.add(ad);
        }
        ads.setResults(adsList);
        return ads;
    }
    public AdEntity addAd(Ad ad, MultipartFile image) throws IOException {
        AdEntity adEntity = new AdEntity();
        String fileName = Instant.now().toEpochMilli() + "_" + image.getOriginalFilename();
        Path path = Paths.get("uploads", fileName);
        Files.write(path, image.getBytes());
        adEntity.setImageFileName(fileName);
        adEntity.setId(ad.getPk());
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        return adEntity;

    }
    public AdEntity updateAd(AdEntity adEntity, CreateOrUpdateAd updateAd){
        adEntity.setTitle(updateAd.getTitle());
        adEntity.setPrice(updateAd.getPrice());
        adEntity.setTarget(updateAd.getDescription());
        return adEntity;
    }
    public ExtendedAd getExtendedAd(AdEntity adEntity){
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(adEntity.getUser().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getUser().getLastName());
        extendedAd.setDescription(adEntity.getTarget());
        extendedAd.setEmail(adEntity.getNameUser());
        extendedAd.setImage(adEntity.getImageFileName());
        extendedAd.setPhone(adEntity.getPhoneUser());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }

}
