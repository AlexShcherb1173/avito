package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    private static final String IMAGE_DIRECTORY = "images/";

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    public ImageServiceImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    @Override
    @Transactional
    public void updateUserImage(byte[] image, Authentication authentication) {
        try {
            Users user = userRepository.findById(Integer.valueOf(authentication.name())).orElseThrow(() -> new RuntimeException("User not found"));
            String imageUrl = saveImage(image, "user_" + user.getId());
            user.setImageUrl(imageUrl);
            userRepository.save(user);

        } catch (Exception e) {
            log.error("Error saving user image", e);
            throw new RuntimeException("Failed to save user image", e);
        }
    }

    @Override
    @Transactional
    public void updateAdImage(Integer adId, byte[] image) {
        try {
            AdEntity ad = adRepository.findById(adId)
                    .orElseThrow(() -> new RuntimeException("Ad not found"));

            String imageUrl = saveImage(image, "ad_" + adId);
            ad.setImageUrl(imageUrl);
            adRepository.save(ad);

        } catch (Exception e) {
            log.error("Error saving ad image for ad: {}", adId, e);
            throw new RuntimeException("Failed to save ad image", e);
        }
    }

    private String saveImage(byte[] imageData, String prefix) throws IOException, IOException {
        Path directory = Paths.get(IMAGE_DIRECTORY);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String fileName = prefix + "_" + UUID.randomUUID() + ".jpg";
        Path filePath = directory.resolve(fileName);

        Files.write(filePath, imageData);

        return filePath.toString();
    }


}
