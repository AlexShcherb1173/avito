package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AdService {

    private static final Logger log = LoggerFactory.getLogger(AdService.class);

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper adMapper;
    private static final String UPLOAD_DIR = "uploads/ads/";

    public AdService(AdRepository adRepository, UserService userService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.adMapper = adMapper;
    }

    public Ads getAllAds() {
        log.info("Получение всех объявлений");
        List<AdEntity> ads = adRepository.findAll();
        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(adMapper.toDtoList(ads));
        return result;
    }

    @Transactional
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException {
        log.info("Создание нового объявления пользователем: {}", authentication.getName());

        UserEntity author = userService.getUserEntity(authentication.getName());

        AdEntity ad = AdEntity.builder()
                .author(author)
                .title(properties.getTitle())
                .price(properties.getPrice())
                .description(properties.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            ad.setImage(imagePath);
        }

        AdEntity savedAd = adRepository.save(ad);
        log.info("Создано новое объявление ID: {}", savedAd.getPk());
        return adMapper.toDto(savedAd);
    }

    public ExtendedAd getExtendedAd(Integer id) {
        log.info("Получение объявления по ID: {}", id);
        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        return adMapper.toExtendedDto(ad);
    }

    @Transactional
    @PreAuthorize("@adService.isAdOwner(#id, authentication) or hasRole('ADMIN')")
    public void removeAd(Integer id) {
        log.info("Удаление объявления ID: {}", id);

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        if (ad.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get(ad.getImage()));
            } catch (IOException e) {
                log.warn("Не удалось удалить файл изображения: {}", ad.getImage());
            }
        }

        adRepository.delete(ad);
        log.info("Объявление ID: {} удалено", id);
    }

    @Transactional
    @PreAuthorize("@adService.isAdOwner(#id, authentication) or hasRole('ADMIN')")
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd) {
        log.info("Обновление объявления ID: {}", id);

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        if (updateAd.getTitle() != null) {
            ad.setTitle(updateAd.getTitle());
        }
        if (updateAd.getPrice() != null) {
            ad.setPrice(updateAd.getPrice());
        }
        if (updateAd.getDescription() != null) {
            ad.setDescription(updateAd.getDescription());
        }

        AdEntity savedAd = adRepository.save(ad);
        log.info("Объявление ID: {} обновлено", id);
        return adMapper.toDto(savedAd);
    }

    public Ads getAdsByUser(String username) {
        log.info("Получение объявлений пользователя: {}", username);
        List<AdEntity> userAds = adRepository.findAllByAuthor_Username(username);
        Ads result = new Ads();
        result.setCount(userAds.size());
        result.setResults(adMapper.toDtoList(userAds));
        return result;
    }

    @Transactional
    @PreAuthorize("@adService.isAdOwner(#id, authentication) or hasRole('ADMIN')")
    public byte[] updateAdImage(Integer id, MultipartFile image) throws IOException {
        log.info("Обновление изображения объявления ID: {}", id);

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        if (ad.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get(ad.getImage()));
            } catch (IOException e) {
                log.warn("Не удалось удалить старый файл: {}", ad.getImage());
            }
        }

        String imagePath = saveImage(image);
        ad.setImage(imagePath);
        adRepository.save(ad);

        log.info("Изображение объявления ID: {} обновлено", id);
        return Files.readAllBytes(Paths.get(imagePath));
    }

    public byte[] getAdImage(Integer id) throws IOException {
        log.debug("Получение изображения объявления ID: {}", id);

        AdEntity ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        if (ad.getImage() == null) {
            return new byte[0];
        }

        Path imagePath = Paths.get(ad.getImage());
        if (!Files.exists(imagePath)) {
            return new byte[0];
        }

        return Files.readAllBytes(imagePath);
    }

    public AdEntity getAdEntity(Integer adId) {
        log.debug("Получение сущности объявления ID: {}", adId);
        return adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));
    }

    public boolean isAdOwner(Integer adId, Authentication authentication) {
        return adRepository.findById(adId)
                .map(ad -> ad.getAuthor().getUsername().equals(authentication.getName()))
                .orElse(false);
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = image.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(image.getInputStream(), filePath);

        return filePath.toString();
    }
}