package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdMapper adMapper;

    private String objectAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    public boolean verificationAuthorAd(Integer id) {
        String authentication = objectAuthentication();
        log.info("authentication {}", authentication);
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));
        return ad.getUser().getEmail().equals(authentication);
    }

    @Override
    public Ads getAllAds() {
        log.info("Получение всех объявлений");
        List<Ad> ads = adRepository.findAll();
        return new Ads(ads.size(), ads.stream()
                .map(adMapper::mapToAdDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void addAds(MultipartFile image, CreateOrUpdateAdDto ad) throws IOException {
        String authentication = objectAuthentication();
        if (ad == null || authentication == null) {
            throw new IllegalArgumentException("Данные объявления или аутентификации не могут быть null");
        }
        log.info("Добавление нового объявления");
        User user = (userRepository.findByEmail(authentication)).orElseThrow();

        Ad newAd = adMapper.dtoToAd(ad, image, user);
//        newAd.setTitle(ad.getTitle());
//        newAd.setPrice(ad.getPrice());
//        newAd.setDescription(ad.getDescription());
//        newAd.setImage(imageService.uploadImage(image));
//        newAd.setUser(user);
        adRepository.save(newAd);
//        return new Ads(1, List.of(mapToAdDto(newAd)));
    }

    @Override
    public Ads getUserAds() {
        String authentication = objectAuthentication();
        log.info("--- Получение объявлений авторизованного пользователя");
        List<Ad> userAds = adRepository.findByUserEmail(authentication);
        return new Ads(userAds.size(), userAds.stream()
                .map(adMapper::mapToAdDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void removeAd(Integer id) {
        log.info("Удаление объявления с ID: {}", id);
        String authentication = objectAuthentication();
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));
//        if (!ad.getUser().getEmail().equals(authentication)) {
//            throw new RuntimeException("Нет прав на удаление объявления");
//        }
        adRepository.delete(ad);
    }


    @Override
    public ExtendedAdDto getAdById(Integer id) {
        log.info("Получение объявления с ID: {}", id);
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));
        String username = objectAuthentication();
        User user = (userRepository.findByEmail(ad.getUser().getEmail())).orElseThrow();
        return adMapper.mapToExtendedAdDto(ad, user);
    }

    @Override
    public void updateAdById(Integer id, CreateOrUpdateAdDto ad) {
        log.info("Обновление объявления с ID: {}", id);
        String authentication = objectAuthentication();

        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));
//        if (!existingAd.getUser().getEmail().equals(authentication)) {
//            throw new RuntimeException("Нет прав на обновление объявления");
//        }
        existingAd.setTitle(ad.getTitle());
        existingAd.setPrice(ad.getPrice());
        existingAd.setDescription(ad.getDescription());
        adRepository.save(existingAd);
//        return new Ads(1, List.of(mapToAdDto(existingAd)));
    }

    @Override
    public void updateImageAd(Integer id, MultipartFile image) throws IOException {
        log.info("Обновление картинки объявления с ID: {}", id);
        String authentication = objectAuthentication();
        Ad existingAd = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));
//        if (!existingAd.getUser().getEmail().equals(authentication)) {
//            throw new RuntimeException("Нет прав на обновление картинки");
//        }
        existingAd.setImage(imageService.uploadImage(image));
        adRepository.save(existingAd);
    }


    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Изображение не может быть пустым");
        }
        // Логика сохранения изображения
        return "https://example.com/image.jpg";
    }
}