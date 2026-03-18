package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdMapper {

    public Ad toDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        Ad ad = new Ad();
        ad.setPk(entity.getId());
        ad.setAuthor(entity.getAuthor().getId());
        ad.setImage(buildImagePath(entity.getImage()));
        ad.setPrice(entity.getPrice());
        ad.setTitle(entity.getTitle());
        return ad;
    }

    public ExtendedAd toExtendedDto(AdEntity entity) {
        if (entity == null) {
            return null;
        }

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(entity.getId());
        extendedAd.setAuthorFirstName(entity.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(entity.getAuthor().getLastName());
        extendedAd.setDescription(entity.getDescription());
        extendedAd.setEmail(entity.getAuthor().getEmail());
        extendedAd.setImage(buildImagePath(entity.getImage()));
        extendedAd.setPhone(entity.getAuthor().getPhone());
        extendedAd.setPrice(entity.getPrice());
        extendedAd.setTitle(entity.getTitle());
        return extendedAd;
    }

    public AdEntity fromDto(CreateOrUpdateAd dto, UserEntity author) {
        if (dto == null || author == null) {
            return null;
        }

        AdEntity entity = new AdEntity();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setAuthor(author);
        return entity;
    }

    public void updateAdFields(CreateOrUpdateAd dto, AdEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
    }

    private String buildImagePath(String image) {
        if (image == null || image.isBlank()) {
            return null;
        }

        if (image.startsWith("/images/")) {
            return image;
        }

        if (image.startsWith("images/")) {
            return "/" + image;
        }

        return "/images/" + image;
    }
}