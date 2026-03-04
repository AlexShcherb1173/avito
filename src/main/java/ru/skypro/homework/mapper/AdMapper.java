package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

@Mapper(componentModel = "spring")
public interface AdMapper {

    // ENTITY → DTO
    @Mappings({
            @Mapping(target = "pk", expression = "java(entity.getId() == null ? null : entity.getId().intValue())"),
            @Mapping(target = "author", expression = "java(entity.getAuthor() == null ? null : entity.getAuthor().getId().intValue())")
    })
    ru.skypro.homework.dto.Ad toDto(ru.skypro.homework.entity.Ad entity);


    // ENTITY → EXTENDED DTO
    @Mappings({
            @Mapping(target = "pk", expression = "java(entity.getId() == null ? null : entity.getId().intValue())"),
            @Mapping(source = "author.firstName", target = "authorFirstName"),
            @Mapping(source = "author.lastName", target = "authorLastName"),
            @Mapping(source = "author.email", target = "email"),
            @Mapping(source = "author.phone", target = "phone")
    })
    ExtendedAd toExtendedDto(ru.skypro.homework.entity.Ad entity);


    // DTO → ENTITY
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "image", ignore = true)
    })
    ru.skypro.homework.entity.Ad toEntity(CreateOrUpdateAd dto);
}