package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.Advertisements;
import ru.skypro.homework.dto.ads.AdvertisementsDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdvert;
import ru.skypro.homework.dto.ads.ExtendAdvert;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    @Value("${ad.image.path}")
    private String imagePath;
    private AdRepository adRepository;
    private UserRepository userRepository;
    private ImageRepository imageRepository;
    private AdMapper adMapper;
    private ImageService imageService;



    @Override
    public Advertisements getAllAds() {
        return adMapper.adsToAdsDto(adRepository.findAll());
    }

    @Override
    public ExtendAdvert getAd(Integer adId) {
        return adMapper.adToExtendedDtoOut(adRepository.findById(adId).orElse(null));
    }

    @Override
    public Advertisements getAdsMe(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return null;
        }
        return adMapper.adsToAdsDto(adRepository.findAllByUserId(user.getId()).get());
    }

    @Override
    public AdvertisementsDTO addAd(CreateOrUpdateAdvert properties, MultipartFile image, Authentication authentication) throws IOException {
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Image image1 = imageService.uploadAdImage(properties, image);
        Ad ad = adMapper.createOrUpdateAdToAd(properties, user, image1);
        adRepository.save(ad);
        return adMapper.adToAdDto(ad);
    }

    @Override
    public  AdvertisementsDTO updateAd(Integer adId, CreateOrUpdateAdvert properties) {
        if (!adRepository.existsById(adId)) {
            throw new AdNotFoundException(adId);
        }
        Ad ad = adRepository.findById(adId).orElse(null);
        Ad newAd = adMapper.createOrUpdateAdToAd(ad, properties);
        adRepository.save(newAd);
        return adMapper.adToAdDto(newAd);
    }

    @Override
    public String updateImage(Integer adId, MultipartFile image) throws IOException {
        if (!adRepository.existsById(adId)) {
            throw new AdNotFoundException(adId);
        }
        Ad ad = adRepository.findById(adId).orElse(null);
        return imageService.updateAdImage(ad.getImage().getId(), image);
    }

    @Override
    public void delete(Integer adId) {
        if (!adRepository.existsById(adId)) {
            throw new AdNotFoundException(adId);
        }

        adRepository.deleteById(adId);
    }
}
