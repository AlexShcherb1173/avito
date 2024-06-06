package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.AdEntity;

public interface CreateOrUpdateService {
    AdEntity mappingToEntity(CreateOrUpdateAdDto dto);

    CreateOrUpdateAdDto mappingToDto(AdEntity entity);
}
