package ru.skypro.homework.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.mapper.AdMapperService;

@Service
public class AdMapperServiceImpl implements AdMapperService {
    @Value(value = "${ads.image}")
    private String linkImage;

    @Override
    public AdDto mappingToDto(AdEntity entity) {
        AdDto result = new AdDto();
        result.setAuthor(entity.getUser().getId());
        result.setPk(entity.getId());
        result.setPrice(entity.getPrice());
        result.setTitle(entity.getTitle());
        ImageEntity imageEntity = entity.getImageEntity();
        if (imageEntity != null) {
            result.setImage(String.format(linkImage, imageEntity.getId()));
        }
        return result;
    }

    @Override
    public AdEntity mappingToEntity(AdDto dto) {
        AdEntity adEntity = new AdEntity();
        adEntity.setId(dto.getPk());
        adEntity.setPrice(dto.getPrice());
        adEntity.setTitle(dto.getTitle());
        return adEntity;
    }
}
