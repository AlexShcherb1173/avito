package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {

    private List<Ad> adList = new ArrayList<>(); // Хранение объявлений в памяти (можно заменить на БД)
    private int currentId = 1; // Счетчик ID для новых объявлений

    public Ads getAllAds() {
        Ads ads = new Ads();
        ads.setResults(adList);
        return ads;
    }

    public Ad addAd(MultipartFile image, CreateOrUpdateAd adDto) {
        // Логика обработки изображения (например, сохранение на диск) может быть добавлена здесь

        Ad newAd = new Ad();
        newAd.setPk(currentId++);
        newAd.setTitle(adDto.getTitle());
        newAd.setPrice(adDto.getPrice());

        // Сохранение нового объявления в списке
        adList.add(newAd);
        return newAd;
    }

    public ExtendedAd getAdById(Integer id) {
        Optional<Ad> adOptional = adList.stream().filter(ad -> ad.getPk().equals(id)).findFirst();
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            ExtendedAd extendedAd = new ExtendedAd();
            extendedAd.setPk(ad.getPk());
            extendedAd.setTitle(ad.getTitle());
            extendedAd.setPrice(ad.getPrice());
            // Дополнительные поля могут быть добавлены здесь
            return extendedAd;
        }
        throw new RuntimeException("Объявление не найдено");
    }

    public void updateAd(Integer id, CreateOrUpdateAd adDto) {
//        Ad existingAd = repository.getAdById(id);
//        existingAd.setTitle(adDto.getTitle());
//        existingAd.setPrice(adDto.getPrice());
    }

    public void removeAd(Integer id) {
//        Ad adToRemove = repository.getAdById(id);
//        adList.remove(adToRemove);
    }
}