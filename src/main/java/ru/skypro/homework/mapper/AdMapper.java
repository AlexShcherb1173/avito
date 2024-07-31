package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {

    Ad createAdsDtoToAd(CreateOrUpdateAdDto createOrUpdateAdDto);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author_id", target = "author")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    AdDto advertToAdsDto(Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author_firstName", target = "authorFirstName")
    @Mapping(source = "author_lastName", target = "authorLastName")
    @Mapping(source = "author_username", target = "email")
    @Mapping(source = "author_phone", target = "phone")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    ExtendedAdDto adToAdsDto(Ad ad);

    List<AdDto> adListToAdsDtoList(List<Ad> ad);

    void updateAd(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Ad ad);

    default AdsDto listToAdsDto(List<Ad> ad) {
        AdsDto result = new AdsDto();
        result.setCount(ad.size());
        result.setResults(adListToAdsDtoList(ad));
        return result;
    }

    default String getUrlToImage(Ad ad) {
        if (ad.getImage() == null) {
            return null;
        }
        return "/ads/" + ad.getId() + "/image";
    }
}
