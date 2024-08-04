package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Advert;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdvertMapper {

    Advert createAdsDtoToAd(CreateOrUpdateAdDto createOrUpdateAdDto);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    AdvertDto advertToAdsDto(Advert advert);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phoneNumber", target = "phone")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    ExtendedAdDto adToAdsDto(Advert advert);

    List<AdvertDto> adListToAdsDtoList(List<Advert> advert);

    void updateAd(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Advert advert);

    default AdvertsDto listToAdsDto(List<Advert> advert) {
        AdvertsDto result = new AdvertsDto();
        result.setCount(advert.size());
        result.setResults(adListToAdsDtoList(advert));
        return result;
    }

    default String getUrlToImage(Advert advert) {
        if (advert.getImage() == null) {
            return null;
        }
        return "/ads/" + advert.getId() + "/image";
    }
}
