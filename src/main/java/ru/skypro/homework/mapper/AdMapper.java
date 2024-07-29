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
    @Mapping(source = "author.id", target = "author")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    AdsDto advertToAdsDto(Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(expression = "java(getUrlToImage(advert))", target = "image")
    ExtendedAdDto adToAdsDto(Ad ad);

    List<CommentDto> advertListToAdsDtoList(List<Ad> adv);

    void updateAdvert(CreateOrUpdateAdDto createOrUpdateAdDto, @MappingTarget Ad ad);

    default CommentsDto listToAdsDto(List<Ad> ad) {
        CommentsDto result = new CommentsDto();
        result.setType(ad.size());
        result.setResults(advertListToAdsDtoList(ad));
        return result;
    }

    default String getUrlToImage(Ad ad) {
        if (ad.getImage() == null) {
            return null;
        }
        return "/ads/" + ad.getId() + "/image";
    }
}
