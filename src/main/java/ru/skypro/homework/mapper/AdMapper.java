package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdMapper {

    //Преобразование сущности в DTO для списка объявлений

    public Ad toAdDto(AdEntity adEntity) {
        if (adEntity == null) {
            return null;
        }

        Ad ad = new Ad();
        ad.setPk(adEntity.getId());
        ad.setAuthor(adEntity.getAuthor().getId());
        ad.setImage(adEntity.getImagePath());
        ad.setPrice(adEntity.getPrice());
        ad.setTitle(adEntity.getTitle());
        return ad;
    }

    //Преобразование сущности в расширенный DTO

    public ExtendedAd toExtendedAdDto(AdEntity adEntity) {
        if (adEntity == null) {
            return null;
        }

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(adEntity.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getAuthor().getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(adEntity.getAuthor().getEmail());
        extendedAd.setImage(adEntity.getImagePath());
        extendedAd.setPhone(adEntity.getAuthor().getPhoneNumber());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }

    //Преобразование DTO для создания/обновления в сущность

    public AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd, UserEntity author) {
        if (createOrUpdateAd == null) {
            return null;
        }

        AdEntity adEntity = new AdEntity();
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setAuthor(author);
        return adEntity;
    }


    // Обновление существующей сущности данными из DTO

    public void updateAdEntityFromDto(AdEntity adEntity, CreateOrUpdateAd updateAd) {
        if (adEntity == null || updateAd == null) {
            return;
        }

        adEntity.setTitle(updateAd.getTitle());
        adEntity.setDescription(updateAd.getDescription());
        adEntity.setPrice(updateAd.getPrice());
    }
} 