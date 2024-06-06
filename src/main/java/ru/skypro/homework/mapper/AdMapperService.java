package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.AdEntity;

public interface AdMapperService {
    AdDto mappingToDto(AdEntity entity);

    AdEntity mappingToEntity(AdDto dto);
}
