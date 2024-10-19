package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface ExtendedAdMapper {

    @Mapping(target = "pk", source = "AdEntity.id")
    @Mapping(target = "authorFirstName", source = "UserEntity.firstname")
    @Mapping(target = "authorLastName", source = "UserEntity.lastname")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "email", source = "UserEntity.email")
    @Mapping(target = "image", source = "AdEntity.image")
    @Mapping(target = "phone", source = "UserEntity.phone")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    ExtendedAd toExtendedAd(AdEntity entity1, UserEntity entity2);

}
