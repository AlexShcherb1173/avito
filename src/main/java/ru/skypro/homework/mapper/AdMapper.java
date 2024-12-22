package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

/**
 * Класс для преобразования (маппинга) между сущностями и DTO - объявления.
 */
@Component
public class AdMapper {
    public static AdDto toDto(Ad ad) {
        AdDto adDto = new AdDto();
        adDto.setPk(ad.getId());
        adDto.setTitle(ad.getTitle());
        adDto.setPrice(ad.getPrice());
        adDto.setAuthor(ad.getUser().getId());
        adDto.setImage(ad.getImageUrl());
        return adDto;
    }

    public static ExtendedAdDto toExtendedAdDto(Ad from) {
        ExtendedAdDto to = new ExtendedAdDto();
        to.setPk(from.getId());
        to.setTitle(from.getTitle());
        to.setPrice(from.getPrice());
        to.setDescription(from.getAdText());
        to.setAuthorFirstName(from.getUser().getFirstName());
        to.setAuthorLastName(from.getUser().getLastName());
        to.setEmail(from.getUser().getUsername());
        to.setPhone(from.getUser().getPhone());
        to.setImage(from.getImageUrl());
        return to;
    }

    public static Ad toAd(CreateOrUpdateAdDto ad, User user) {
        return new Ad(ad.getPrice(), ad.getTitle(), ad.getDescription(), user);
    }

    public static CreateOrUpdateAdDto toCreateOrUpdateAdDto(Ad ad) {

        return new CreateOrUpdateAdDto(ad.getTitle(), ad.getPrice(), ad.getAdText());
    }
}