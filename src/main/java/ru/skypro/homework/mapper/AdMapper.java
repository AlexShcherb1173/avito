package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AdMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    Ad toDto(AdEntity entity);

    List<Ad> toDtoList(List<AdEntity> entities);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    ExtendedAd toExtendedDto(AdEntity entity);
}