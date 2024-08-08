package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.*;

@Mapper(componentModel = "spring")
public interface AdvertMapper {

    AdvertMapper INSTANCE = Mappers.getMapper(AdvertMapper.class);

    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(target = "image", expression = "java(\"/image/\" + advert.getImage().getId())")
    AdvertDto adToAdDTO(Advert advert);

    @Mapping(source = "user", target = "author")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    Advert createOrUpdateAdDTOToAd(CreateOrUpdateAdDto createOrUpdateAdDTO, User user);

    @Mapping(source = "user.id", target = "pk")
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(target = "image", expression = "java(\"/image/\" + advert.getImage().getId())")
    ExtendedAdDto toExtendedAdDTO(Advert advert, User user);
}
