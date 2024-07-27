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

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", expression = "java(getUrlToImage(advert))")
    AdsDto advertToAdsDto(Ad ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.username")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", expression = "java(getUrlToImage(advert))")
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
