package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.model.AdEntity;

import javax.persistence.EntityNotFoundException;

public class AdMapper {

    public AdEntity toAdEntity(Ad ad) {
        if (ad == null) {
            throw new EntityNotFoundException("Переданный объект Ad is null");
        }
        AdEntity adEntity = new AdEntity();
        adEntity.setAuthor(ad.getAuthor());
        adEntity.setImage(ad.getImage());
        adEntity.setId(ad.getId());
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        return adEntity;
    }

    public Ad toAdDto(AdEntity adEntity) {
        if (adEntity == null) {
            throw new EntityNotFoundException("Переданный объект AdEntity is null");
        }
        Ad ad = new Ad();
        ad.setAuthor(adEntity.getAuthor());
        ad.setImage(adEntity.getImage());
        ad.setId(adEntity.getId());
        ad.setPrice(adEntity.getPrice());
        ad.setTitle(adEntity.getTitle());
        return ad;
    }

}
