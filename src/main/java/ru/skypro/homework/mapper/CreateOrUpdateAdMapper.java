package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface CreateOrUpdateAdMapper {

    @Mapping(target = "title", source = "title")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    CreateOrUpdateAd toCreateOrUpdateAd(AdEntity entity);
}
