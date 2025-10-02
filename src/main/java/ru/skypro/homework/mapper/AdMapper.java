package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.User;

public class AdMapper {

    public static Ad toDto(ru.skypro.homework.model.Ad entity) {
        Ad dto = new Ad();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static ru.skypro.homework.model.Ad toEntity(CreateOrUpdateAd dto, User author) {
        ru.skypro.homework.model.Ad entity = new ru.skypro.homework.model.Ad();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setAuthor(author);
        return entity;
    }
}