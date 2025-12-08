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
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация {@link AdsService} для управления объявлениями.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;
    private final ImageService imageService;

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image, String username) {
        log.info("Adding new ad for user: {}", username);

        UserEntity author = userService.getUserEntity(username);
        AdEntity adEntity = adMapper.toEntity(properties);
        adEntity.setAuthor(author);

        if (image != null && !image.isEmpty()) {
            String imagePath = imageService.saveImage(image);
            adEntity.setImage(imagePath);
        }

        AdEntity savedAd = adRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getExtendedAd(Integer id) {
        log.info("Getting extended ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        return adMapper.toExtendedAd(adEntity);
    }

    /**
     * {@inheritDoc}
     */
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
            imageService.deleteImage(adEntity.getImage());
        }

        adRepository.delete(adEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd updateAd, String username) {
        log.info("Updating ad with id: {} by user: {}", id, username);
        UserEntity user = userService.getUserEntity(username);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (!user.getRole().equals(Role.ADMIN) && !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to update this ad");
        }

        adMapper.updateEntityFromDto(updateAd, adEntity);
        AdEntity updatedAd = adRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAdImage(Integer id, MultipartFile image, String username) {
        log.info("Updating image for ad with id: {} by user: {}", id, username);
        UserEntity user = userService.getUserEntity(username);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (!user.getRole().equals(Role.ADMIN) && !adEntity.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("No permission to update this ad");
        }

        if (adEntity.getImage() != null) {
            imageService.deleteImage(adEntity.getImage());
        }

        String newImagePath = imageService.saveImage(image);
        adEntity.setImage(newImagePath);
        adRepository.save(adEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getAdImage(Integer id) {
        log.info("Getting image for ad with id: {}", id);
        AdEntity adEntity = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));

        if (adEntity.getImage() == null) {
            throw new EntityNotFoundException("Image not found for ad with id: " + id);
        }

        return imageService.getImage(adEntity.getImage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public AdEntity getAdEntity(Integer id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isAdOwner(Integer adId, String username) {
        try {
            AdEntity adEntity = adRepository.findById(adId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
            UserEntity userEntity = userService.getUserEntity(username);
            return adEntity.getAuthor().getId().equals(userEntity.getId());
        } catch (Exception e) {
            log.warn("Error checking ad ownership: {}", e.getMessage());
            return false;
        }
    }
}