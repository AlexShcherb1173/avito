package ru.skypro.homework.mapper.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.CreateOrUpdateService;

@Service
public class CreateOrUpdateServiceImpl implements CreateOrUpdateService {

    @Override
    public AdEntity mappingToEntity(CreateOrUpdateAdDto dto) {
        AdEntity adEntity = new AdEntity();
        adEntity.setTitle(dto.getTitle());
        adEntity.setPrice(dto.getPrice());
        adEntity.setDescription(dto.getDescription());
        return adEntity;
    }

    @Override
    public CreateOrUpdateAdDto mappingToDto(AdEntity entity) {
        CreateOrUpdateAdDto dto = new CreateOrUpdateAdDto();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
