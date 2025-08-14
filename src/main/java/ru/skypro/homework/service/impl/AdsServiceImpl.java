package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final UsersRepository usersRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final AdMapper adMapper;

    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        log.info("Getting all ads");
        List<Ad> ads = adRepository.findAll();

        List<AdDTO> adDTOs = ads.stream()
                .map(ad -> {
                    AdDTO dto = adMapper.adEntityToAdDTO(ad);
                    if (ad.getImage() != null) {
                        dto.setImage("/images/" + ad.getImage().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Found {} ads", adDTOs.size());
        return new Ads(adDTOs.size(), adDTOs);
    }

    @Override
    @Transactional
    public AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile imageFile, String username) throws IOException {
        validateInput(createAd, username);

        Users author = usersRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Ad ad = createAdEntity(createAd, author);

        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = processImage(imageFile);
            ad.setImage(image);
            log.debug("Image attached to ad: {}", image.getId());
        } else {
            log.warn("Ad created without image by user: {}", username);
        }

        Ad savedAd = adRepository.save(ad);
        log.info("Ad saved successfully. ID: {}, Author: {}", savedAd.getId(), username);

        return adMapper.adEntityToAdDTO(savedAd);
    }

    private void validateInput(CreateOrUpdateAd createAd, String username) {
        if (createAd == null) {
            throw new IllegalArgumentException("Ad data cannot be null");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
    }

    private Ad createAdEntity(CreateOrUpdateAd createAd, Users author) {
        Ad ad = new Ad();
        ad.setTitle(createAd.getTitle());
        ad.setPrice(createAd.getPrice());
        ad.setDescription(createAd.getDescription());
        ad.setAuthor(author);
        return ad;
    }

    private Image processImage(MultipartFile imageFile) throws IOException {
        if (!Objects.requireNonNull(imageFile.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        String filename = "ad_" + UUID.randomUUID() + "_" +
                (imageFile.getOriginalFilename() != null ?
                        imageFile.getOriginalFilename() : "image");

        Path uploadPath = Paths.get("./uploads/ads/");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, imageFile.getBytes());

        Image image = new Image();
        image.setFilePath("ads/" + filename);
        image.setMediaType(imageFile.getContentType());
        image.setFileSize(imageFile.getSize());

        return imageRepository.save(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getExtendedAd(Integer id) {
        log.info("Getting extended ad with ID: {}", id);
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ad not found with ID: {}", id);
                    return new AdNotFoundException(id);
                });

        return adMapper.adEntityToExtendedAd(ad);
    }

    @Override
    @Transactional
    public void removeAd(Integer id, Authentication authentication) {
        String username = authentication.getName();
        log.info("Deleting ad ID: {} for user: {}", id, username);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        // Проверяем, является ли пользователь администратором
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // Разрешаем удаление если: пользователь - владелец или администратор
        if (!isAdmin && !ad.getAuthor().getEmail().equals(username)) {
            throw new ForbiddenException("You are not the owner of this ad");
        }

        try {
            commentRepository.deleteByAdId(id);
            if (ad.getImage() != null) {
                try {
                    Files.deleteIfExists(Paths.get("./uploads", ad.getImage().getFilePath()));
                } catch (IOException e) {
                    log.error("Failed to delete image file: {}", ad.getImage().getFilePath(), e);
                }
            }
            adRepository.delete(ad);
            log.info("Ad {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Failed to delete ad {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete ad", e);
        }
    }

    @Override
    @Transactional
    public AdDTO updateAd(Integer id, CreateOrUpdateAd updateAd, Authentication authentication) {
        String username = authentication.getName();
        log.info("Updating ad with ID: {} for user: {}", id, username);

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !ad.getAuthor().getEmail().equals(username)) {
            throw new ForbiddenException("You are not the owner of this ad");
        }

        ad.setTitle(updateAd.getTitle());
        ad.setPrice(updateAd.getPrice());
        ad.setDescription(updateAd.getDescription());

        Ad updatedAd = adRepository.save(ad);
        log.info("Ad with ID {} updated successfully", id);

        return adMapper.adEntityToAdDTO(updatedAd);
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getUserAds(String username) {
        log.info("Getting ads for user: {}", username);

        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Ad> ads = adRepository.findAllByAuthorId(user.getId());
        log.debug("Found {} ads for user {}", ads.size(), username);

        List<AdDTO> adDTOs = ads.stream()
                .map(ad -> {
                    AdDTO dto = adMapper.adEntityToAdDTO(ad);
                    if (ad.getImage() != null) {
                        dto.setImage("/images/" + ad.getImage().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        return new Ads(adDTOs.size(), adDTOs);
    }

    @Override
    @Transactional
    public byte[] updateAdImage(Integer id, MultipartFile imageFile, String username) throws IOException {
        log.info("Updating image for ad ID: {} by user: {}", id, username);

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        if (!Objects.requireNonNull(imageFile.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));

        if (!ad.getAuthor().getEmail().equals(username)) {
            throw new ForbiddenException("You are not the owner of this ad");
        }

        // Удаляем старое изображение
        if (ad.getImage() != null) {
            try {
                Files.deleteIfExists(Paths.get("./uploads", ad.getImage().getFilePath()));
            } catch (IOException e) {
                log.error("Failed to delete old image file: {}", ad.getImage().getFilePath(), e);
            }
            imageRepository.delete(ad.getImage());
        }

        // Сохраняем новое изображение
        String filename = "ad_" + UUID.randomUUID() + "_" +
                (imageFile.getOriginalFilename() != null ?
                        imageFile.getOriginalFilename() : "image");

        Path uploadPath = Paths.get("./uploads/ads/");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, imageFile.getBytes());

        // Создаем запись в БД
        Image newImage = new Image();
        newImage.setFilePath("ads/" + filename);
        newImage.setMediaType(imageFile.getContentType());
        newImage.setFileSize(imageFile.getSize());

        Image savedImage = imageRepository.save(newImage);
        ad.setImage(savedImage);
        adRepository.save(ad);

        log.info("Image updated successfully for ad ID: {}", id);
        return Files.readAllBytes(filePath);
    }
}