package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    Ad toDTO(AdEntity entity);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    AdEntity toEntity(Ad dto);

}
