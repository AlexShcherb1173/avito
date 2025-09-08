package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    private static final String UPLOAD_DIR = "uploads/ads/";

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adRepository.findAll();
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(adEntities.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList()));
        return ads;
    }

    @Override
    @Transactional
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException {
        UserEntity author = userService.getCurrentUserEntity(authentication);

        AdEntity adEntity = adMapper.toEntity(properties);
        adEntity.setAuthor(author);

        // Save image
        if (image != null && !image.isEmpty()) {
            String filename = saveImage(image);
            adEntity.setImagePath(filename);
        }

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    @Override
    public ExtendedAd getAd(Integer id) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
        return adMapper.toExtendedAd(adEntity);
    }

    @Override
    public AdEntity getAdEntity(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @Override
    @Transactional
    public void removeAd(Integer id, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        // Check permissions
        if (!authService.isAdmin(authentication) &&
                !authService.isCurrentUser(authentication, adEntity.getAuthor().getId())) {
            throw new RuntimeException("Access denied");
        }

        adRepository.delete(adEntity);

        // Delete image file
        if (adEntity.getImagePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR, adEntity.getImagePath()));
            } catch (IOException e) {
                // Log error but don't fail the operation
            }
        }
    }

    @Override
    @Transactional
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication) {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        // Check permissions
        if (!authService.isAdmin(authentication) &&
                !authService.isCurrentUser(authentication, adEntity.getAuthor().getId())) {
            throw new RuntimeException("Access denied");
        }

        adMapper.updateEntityFromDto(updateAd, adEntity);
        AdEntity updatedAd = adRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }

    @Override
    public Ads getAdsMe(Authentication authentication) {
        UserEntity currentUser = userService.getCurrentUserEntity(authentication);
        List<AdEntity> userAds = adRepository.findByAuthorId(currentUser.getId());

        Ads ads = new Ads();
        ads.setCount(userAds.size());
        ads.setResults(userAds.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList()));
        return ads;
    }

    @Override
    @Transactional
    public byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication) throws IOException {
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!authService.isAdmin(authentication) &&
                !authService.isCurrentUser(authentication, adEntity.getAuthor().getId())) {
            throw new RuntimeException("Access denied");
        }

        if (adEntity.getImagePath() != null) {
            Files.deleteIfExists(Paths.get(UPLOAD_DIR, adEntity.getImagePath()));
        }

        String filename = saveImage(image);
        adEntity.setImagePath(filename);
        adRepository.save(adEntity);

        return image.getBytes();
    }

    @Override
    public byte[] getAdImage(String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, filename);
        return Files.readAllBytes(path);
    }

    private String saveImage(MultipartFile image) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);


        Files.copy(image.getInputStream(), filePath);

        return filename;
    }
}