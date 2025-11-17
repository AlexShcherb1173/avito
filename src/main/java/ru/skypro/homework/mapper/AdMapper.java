package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;

import java.util.List;

/**
 * Mapper for Ad entity (made using mapstruct)
 * Maps AdDto, CreateOrUpdateAdDto, ExtendedAdDto and List of AdDto
 */
@Mapper(componentModel = "spring")
public interface AdMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression = "java(\"/pictures/\" + ad.getImage().getId())")
    AdDto adToAdDto(Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(target = "image", expression = "java(\"/pictures/\" + ad.getImage().getId())")
    ExtendedAdDto adtoExtendedAdDto(Ad ad);

    List<AdDto> adListToAdDtoList(List<Ad> ads);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ad createOrUpdateAdDtoToAdEntity(CreateOrUpdateAdDto createOrUpdateAdDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateAdFromCreateOrUpdateAdDto(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Ad ad);
}
