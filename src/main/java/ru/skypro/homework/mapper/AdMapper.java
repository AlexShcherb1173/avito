package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdMapper {

    AdEntity toAd(CreateOrUpdateAdDto createAdsDto);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression="java(getImage(ad))")
    AdDto toAdsDto(AdEntity ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "image", expression="java(getImage(ad))")
    ExtendedAdDto toFullAdsDto(AdEntity ad);

    default String getImage(AdEntity ad) {
        if (ad.getImage() == null) {
            return null;
        }
        return "/ads/image/" + ad.getId() + "/from-db";
    }

    List<AdDto> adListToAdsDtoList(List<AdEntity> adList);


}
