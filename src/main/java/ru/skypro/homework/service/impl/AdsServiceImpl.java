package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        log.info("Getting all ads");
        List<AdEntity> adEntities = adRepository.findAll();
        List<Ad> ads = adEntities.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, String username) {
        log.info("Adding new ad for user: {}", username);

        UserEntity author = userService.getUserEntity(username);
        AdEntity adEntity = adMapper.toEntity(properties);
        adEntity.setAuthor(author);

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            adEntity.setImage(imagePath);
        }

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getExtendedAd(Integer id) {
        log.info("Getting extended ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        return adMapper.toExtendedAd(adEntity);
    }

    @Override
    public void removeAd(Integer id, String username) {
        log.info("Removing ad with id: {} by user: {}", id, username);
        UserEntity user = userService.getUserEntity(username);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (!user.getRole().equals(Role.ADMIN) && !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to delete this ad");
        }

        if (adEntity.getImage() != null) {
            deleteImage(adEntity.getImage());
        }

        adRepository.delete(adEntity);
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd, String username) {
        log.info("Updating ad with id: {} by user: {}", id, username);
        UserEntity user = userService.getUserEntity(username);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (!adEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to update this ad");
        }

        adMapper.updateEntityFromDto(updateAd, adEntity);
        AdEntity updatedAd = adRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getAdsByUser(String username) {
        log.info("Getting ads for user: {}", username);
        UserEntity user = userService.getUserEntity(username);
        List<AdEntity> userAds = adRepository.findByAuthorId(user.getId());

        List<Ad> ads = userAds.stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());

        Ads result = new Ads();
        result.setCount(ads.size());
        result.setResults(ads);
        return result;
    }

    @Override
    public void updateAdImage(Integer id, MultipartFile image, String username) {
        log.info("Updating image for ad with id: {} by user: {}", id, username);
        UserEntity user = userService.getUserEntity(username);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (!adEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to update this ad");
        }

        if (adEntity.getImage() != null) {
            deleteImage(adEntity.getImage());
        }

        String newImagePath = saveImage(image);
        adEntity.setImage(newImagePath);
        adRepository.save(adEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public AdEntity getAdEntity(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
    }

    private String saveImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID() + extension;
            Path path = Paths.get("images/" + filename);

            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());

            return filename;
        } catch (IOException e) {
            log.error("Failed to save image", e);
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void deleteImage(String imagePath) {
        try {
            Path path = Paths.get("images/" + imagePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Failed to delete image: {}", imagePath, e);
        }
    }
}