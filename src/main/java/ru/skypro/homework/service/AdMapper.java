package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Service
public class AdMapper {

    public Ad toDto(AdEntity e) {
        if (e == null) return null;
        Ad dto = new Ad();
        dto.setPk(e.getId().intValue());
        dto.setAuthor(e.getAuthor().getId().intValue());
        dto.setImage(e.getImage());
        dto.setPrice(e.getPrice());
        dto.setTitle(e.getTitle());
        return dto;
    }

    public ExtendedAd toExtendedDto(AdEntity e) {
        if (e == null) return null;
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(e.getId().intValue());
        dto.setAuthorFirstName(e.getAuthor().getFirstName());
        dto.setAuthorLastName(e.getAuthor().getLastName());
        dto.setDescription(e.getDescription());
        dto.setEmail(e.getAuthor().getUsername());
        dto.setPhone(e.getAuthor().getPhone());
        dto.setImage(e.getImage());
        dto.setPrice(e.getPrice());
        dto.setTitle(e.getTitle());
        return dto;
    }

    public AdEntity fromCreateRequest(CreateOrUpdateAd req, UserEntity author, String imagePath) {
        if (req == null || author == null) return null;
        return AdEntity.builder()
                .title(req.getTitle())
                .price(req.getPrice())
                .description(req.getDescription())
                .image(imagePath)
                .author(author)
                .build();
    }

    public void updateFrom(CreateOrUpdateAd req, AdEntity e, String imagePath) {
        if (req == null || e == null) return;
        e.setTitle(req.getTitle());
        e.setPrice(req.getPrice());
        e.setDescription(req.getDescription());
        if (imagePath != null) {
            e.setImage(imagePath);
        }
    }
}