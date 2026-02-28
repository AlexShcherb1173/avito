package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdMapperManual {


    public Ad toAdDto(AdEntity entity) {
        if (entity == null) return null;

        Ad dto = new Ad();
        dto.setPk(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setAuthor(entity.getAuthor() == null ? null : entity.getAuthor().getId());

        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            dto.setImage("/" + entity.getImage());
        } else {
            dto.setImage("/default.jpg");
        }

        return dto;
    }

    public ExtendedAd toExtendedDto(AdEntity entity) {
        if (entity == null) return null;

        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());

        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            dto.setImage("/" + entity.getImage());
        } else {
            dto.setImage("/default.jpg");
        }

        UserEntity author = entity.getAuthor();
        if (author != null) {
            dto.setAuthorFirstName(author.getFirstName());
            dto.setAuthorLastName(author.getLastName());
            dto.setEmail(author.getEmail());
            dto.setPhone(author.getPhone());
        }

        return dto;
    }

    public void applyCreateOrUpdate(AdEntity entity, CreateOrUpdateAd source) {
        if (entity == null || source == null) return;
        entity.setTitle(source.getTitle());
        entity.setPrice(source.getPrice());
        entity.setDescription(source.getDescription());
    }
}

