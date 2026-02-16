package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.AuthenticationFacade;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final AuthenticationFacade authenticationFacade;

    public AdService(AdRepository adRepository, AuthenticationFacade authenticationFacade) {
        this.adRepository = adRepository;
        this.authenticationFacade = authenticationFacade;
    }

    public List<AdDTO> getAllAds() {
        if (adRepository.findAll().isEmpty()) {
            return new ArrayList<AdDTO>();
        } else {
            return adRepository.findAll().stream()
                    .map(AdMapper.INSTANCE::adToAdDto)
                    .toList();
        }
    }

    public Ad addAd(CreateOrUpdateAd dto, MultipartFile file) {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setPrice(dto.getPrice());
        ad.setAuthor(authenticationFacade.getCurrentUser().getId());

        if (file != null && !file.isEmpty()) {
            try {
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                if (originalImage == null) {
                    throw new RuntimeException("Неверный формат изображения");
                }

                int targetWidth = 300;
                int targetHeight = 300;

                Image scaledInstance = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = scaledImage.createGraphics();
                g2d.drawImage(scaledInstance, 0, 0, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, "jpg", baos);
                byte[] scaledBytes = baos.toByteArray();

                ad.setImage(scaledBytes);
                adRepository.save(ad);

            } catch (IOException e) {
                throw new RuntimeException("Ошибка обработки изображения", e);
            }
        }

        return adRepository.save(ad);
    }

    public Ad getAdById(int id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad не найден"));
    }
}
