package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Advertisement;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
public class AdServiceImpl implements AdService {

    private final AdRepository advertisementRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    private static final String UPLOAD_DIR = "image/";
    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);

    @Autowired
    public AdServiceImpl(AdRepository advertisementRepository, UserRepository userRepository, AdMapper adMapper) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
    }

    @Override
    public Ads getAllAds() {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        return Ads.builder()
                .count(advertisements.size())
                .results(adMapper.advertisementToAdList(advertisements))
                .build();

    }

    public Ad createAd(CreateOrUpdateAd dto, MultipartFile image, String username) {
        log.info("Trying to find user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Found user: {}", user);
        String imagePath = saveImage(image);
        log.info("Mapping ad: {}, author: {}", dto, user.getUsername());
        Advertisement advertisement = adMapper.createOrUpdateAdToAdvertisement(dto, user);
        advertisement.setAuthor(user);
        log.info("Mapped advertisement: {}", advertisement);
        advertisement.setImage(imagePath);

        Advertisement savedAd = advertisementRepository.save(advertisement);
        return adMapper.advertisementToAd(savedAd);
    }
    @Override
    public Ads getAdsByUser(String username) {
        List<Advertisement> advertisements = advertisementRepository.findByAuthorUsername(username);
        return Ads.builder()
                .count(advertisements.size())
                .results(adMapper.advertisementToAdList(advertisements))
                .build();
    }

    private String saveImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Path.of(UPLOAD_DIR, filename);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
    @Override
    public ExtendedAd getAd(long id) {
        return adMapper.advertisementToExtendedAd(advertisementRepository.findById(id).orElse(null));
    }

    @Override
    public boolean removeAd(long id) {
        if (!advertisementRepository.existsById(id)) {
            return false;
        }
        advertisementRepository.deleteById(id);
        return true;
    }

    @Override
    public Ad updateAd(long id, CreateOrUpdateAd dto) {
        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advertisement not found"));
        existingAd.setTitle(dto.getTitle());
        existingAd.setPrice(Double.valueOf(dto.getPrice()));
        existingAd.setDescription(dto.getDescription());

        Advertisement updatedAd = advertisementRepository.save(existingAd);
        return adMapper.advertisementToAd(updatedAd);
    }

}