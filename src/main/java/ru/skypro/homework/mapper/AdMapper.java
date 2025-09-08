package ru.skypro.homework.mapper;



import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;

import ru.skypro.homework.enity.Ad;
import ru.skypro.homework.enity.User;
import java.time.LocalDateTime;

@Component
public final class AdMapper {
    private AdMapper() {}

    public static AdDto toDto(Ad ad) {
        if (ad == null) return null;

        AdDto dto = new AdDto();
        dto.setId(ad.getId() == null ? null : ad.getId().intValue());
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDescription());
        dto.setPrice(ad.getPrice());
        dto.setImage(ad.getImageUrl());
        return dto;
    }


    public static Ad toEntity(AdDto dto, User author) {
        if (dto == null) return null;

        Ad ad = new Ad();
        if (dto.getId() != null) {
            ad.setId(dto.getId().longValue());
        }
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setImageUrl(dto.getImage());
        ad.setAuthor(author);
        if (ad.getCreatedAt() == null) {
            ad.setCreatedAt(LocalDateTime.now());
        }
        return ad;
    }


    public static void updateEntity(Ad ad, AdDto dto) {
        if (ad == null || dto == null) return;
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        ad.setImageUrl(dto.getImage());
    }
}