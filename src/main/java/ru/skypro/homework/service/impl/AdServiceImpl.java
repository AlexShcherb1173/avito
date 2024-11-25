package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    @Autowired
    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public Ads getAllAds() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(adEntity -> adMapper.toAdDto(adEntity))
                .collect(Collectors.toList());
        return new Ads(adsList.size(), adsList.toArray(Ad[]::new));
    }

//    public void removeAd(Integer id) {
//        AdEntity ad = adRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Объявление не найдено"));
//        adRepository.delete(ad);
//    }

    public Ad addAd(CreateOrUpdateAd adProperties, MultipartFile image, String username) throws IOException {
        log.info("Вошли в метод addAd сервиса AdServiceImpl. " +
                "Получены данные (объект) createAD: {}." +
                "Файл объявления {}." +
                "Имя авторизированного пользователя: {}", adProperties, image.getOriginalFilename(), username);
        UUID uuid = UUID.randomUUID();
        String filePathString = "/ad_images" + uuid + "." + getExtension(image);
        Path filePath = Path.of("ad_images", uuid + "." + getExtension(image));

        Files.createDirectories((filePath.getParent()));

        try (InputStream is = image.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
            log.info("Изображение объявления успешно сохранено на диск, полное имя файла: {}", filePathString);
        }
        AdEntity adEntity = adMapper.toAdEntity(adProperties, filePathString, username);
        log.info("Получена сущность: {}", adEntity);
        adRepository.save(adEntity);
        log.info("Сущность сохранена в БД");

        AdEntity adEntityBD = adRepository.findAdEntityByImage(filePathString);

        return adMapper.toAdDto(adEntityBD);
    }

    public Ads getAds(Authentication authentication) {
        List<Ad> myAdsList = adRepository.findAll().stream()
                .filter(adEntity -> adEntity.getUser().getUsername().equals(authentication.getName()))
                .map(adEntity -> adMapper.toAdDto(adEntity))
                .collect(Collectors.toList());
        return new Ads(myAdsList.size(), myAdsList.toArray(Ad[]::new));
    }

//    public void updateAd(Integer id, AdEntity adModel) {
//        AdEntity ad = adRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Объявление с " + id + " не найдено."));
//        if (adModel.getTitle() != null) {
//            ad.setTitle(adModel.getTitle());
//        }
//        if (adModel.getDescription() != null) {
//            ad.setDescription(adModel.getDescription());
//        }
//        if (adModel.getPrice() != null) {
//            ad.setPrice(adModel.getPrice());
//        }
//        adRepository.save(ad);
//    }

    public byte[] findAdImageByFilename(String fileName) throws IOException {
        log.info("Вошли в метод findAvatarImageByFilename сервиса AdServiceImpl, получен fileName (String): {}",
                fileName);
        return Files.readAllBytes(Path.of("ad_images/" + fileName));
    }

    public ExtendedAd getAdById(int id) {

        AdEntity adEntity = adRepository.findAdEntityById(id);

        return adMapper.toExtendedAd(adEntity);
    }

    public String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }
}
