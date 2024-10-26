package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface ExtendedAdMapper {

    ExtendedAdMapper INSTANCE = Mappers.getMapper(ExtendedAdMapper.class);

    @Mapping(target = "pk", source = "entity2.id")
    @Mapping(target = "authorFirstName", source = "entity1.firstname")
    @Mapping(target = "authorLastName", source = "entity1.lastname")
    @Mapping(target = "description", source = "entity2.description")
    @Mapping(target = "email", source = "entity1.email")
    @Mapping(target = "image", source = "entity2.image")
    @Mapping(target = "phone", source = "entity1.phone")
    @Mapping(target = "price", source = "entity2.price")
    @Mapping(target = "title", source = "entity2.title")
    ExtendedAd toExtendedAd(UserEntity entity1, AdEntity entity2);

}
