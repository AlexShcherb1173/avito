package ru.skypro.homework.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final CollectionMapper collectionMapper;
    private final ImageService imageService;
    private final FileStorageConfig fileStorageConfig;

    private static final String ADS_IMAGE_DIR = "ads";
    private static final String BEGIN = "ad_image_";

    @Override
    @Transactional(readOnly = true)
    public AdsDto getAllAds() {
        List<AdEntity> ads = adRepository.findAll();
        return collectionMapper.toAdsDto(ads);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAdDto getAd(Integer id) {
        AdEntity adEntity = getAdById(id);
        return adMapper.toExtendedAdDto(adEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public AdsDto getMyAds(String username) {
        UserEntity user = getUserByUsername(username);
        List<AdEntity> userAds = adRepository.findByAuthorId(user.getId());
        return collectionMapper.toAdsDto(userAds);
    }

    @Override
    public AdDto createAd(CreateOrUpdateAdDto createOrUpdateAdDto, String username, MultipartFile image) throws IOException {
        UserEntity author = getUserByUsername(username);

        AdEntity adEntity = adMapper.toEntity(createOrUpdateAdDto);
        adEntity.setAuthor(author);

        // Сохраняем изображение
        String imageFilename = imageService.saveImage(image, ADS_IMAGE_DIR, BEGIN);
        adEntity.setImage(imageFilename);

        AdEntity savedAd = adRepository.save(adEntity);
        log.info("Created ad with id: {} for user: {}", savedAd.getId(), username);

        return adMapper.toDto(savedAd);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdAuthor(#id, #username)")
    public void deleteAd(Integer id, String username) {
        AdEntity adEntity = getAdById(id);

        try {
            imageService.deleteImage(adEntity.getImage(), ADS_IMAGE_DIR);
        } catch (IOException e) {
            log.warn("Failed to delete image for ad {}: {}", id, e.getMessage());
        }
        adRepository.delete(adEntity);
        log.info("Deleted ad with id: {}, by user: {}", id, username);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdAuthor(#id, #username)")
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, String username) {
        AdEntity adEntity = getAdById(id);

        adMapper.updateEntityFromDto(adEntity, createOrUpdateAdDto);
        AdEntity updatedAdEntity = adRepository.save(adEntity);

        log.info("Updated ad with id: {}, by user: {}", id, username);
        return adMapper.toDto(updatedAdEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getAdImage(Integer id) throws IOException {
        AdEntity adEntity = getAdById(id);
        String image = adEntity.getImage();

        if (image == null || image.isEmpty()) {
            throw new IOException("Ad has no image: " + id);
        }
        return imageService.getImage(image, ADS_IMAGE_DIR);
    }

    @Override
    @Transactional(readOnly = true)
    public String getAdImageContentType(Integer id) throws IOException {
        AdEntity adEntity = getAdById(id);
        String image = adEntity.getImage();

        if (image == null || image.isEmpty()) {
            throw new IOException("Ad has no image: " + id);
        }

        return imageService.getImageContentType(image);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdAuthor(#id, #username)")
    public AdDto updateAdImage(Integer id, MultipartFile image, String username) throws IOException {
        AdEntity adEntity = getAdById(id);

        imageService.deleteImage(adEntity.getImage(), ADS_IMAGE_DIR);

        String newImageFileName = imageService.saveImage(image, ADS_IMAGE_DIR, BEGIN);
        adEntity.setImage(newImageFileName);

        AdEntity savedAd = adRepository.save(adEntity);
        log.info("Updated image for ad: {}, by user: {}", id, username);

        return adMapper.toDto(savedAd);
    }

    //метод для SpEL выражения в @PreAuthorize
    public boolean isAdAuthor(Integer adId, String username) {
        AdEntity adEntity = getAdById(adId);
        UserEntity userEntity = getUserByUsername(username);
        return adEntity.getAuthor().getId().equals(userEntity.getId());
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found exception: " + username));
    }

    private AdEntity getAdById(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
    }
}
