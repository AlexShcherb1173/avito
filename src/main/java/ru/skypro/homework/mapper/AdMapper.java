package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "image", expression = "java(getImageUrl(entity.getImage()))")
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "price", source = "entity.price")
    @Mapping(target = "title", source = "entity.title")
    Ad toDto(AdEntity entity);

    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "authorFirstName", source = "entity.author.firstName")
    @Mapping(target = "authorLastName", source = "entity.author.lastName")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "email", source = "entity.author.email")
    @Mapping(target = "image", expression = "java(getImageUrl(entity.getImage()))")
    @Mapping(target = "phone", source = "entity.author.phone")
    @Mapping(target = "price", source = "entity.price")
    @Mapping(target = "title", source = "entity.title")
    ExtendedAd toExtendedAd(AdEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntityFromDto(CreateOrUpdateAd createOrUpdateAd, @org.mapstruct.MappingTarget AdEntity entity);

    default String getImageUrl(String image) {
        return image != null ? "/images/" + image : null;
    }
}