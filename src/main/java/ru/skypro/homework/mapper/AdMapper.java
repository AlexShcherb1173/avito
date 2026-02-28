package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.entity.AdEntity;


import java.util.List;
import java.util.stream.Collectors;

public class AdMapper {
    public Ad toAdDto(AdEntity entity) {
        Ad dto = new Ad();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setImage(entity.getImage());
        dto.setPk(entity.getPk());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public Ads toAdsDto(List<AdEntity> entities) {
        Ads ads = new Ads();
        ads.setCount(entities.size());
        ads.setResults(entities.stream()
                .map(entity->this.toAdDto(entity))
                .collect(Collectors.toList()));
        return ads;
    }
}
