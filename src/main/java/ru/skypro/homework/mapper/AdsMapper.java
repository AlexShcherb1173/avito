package ru.skypro.homework.mapper;
import org.mapstruct.Mapper;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ads;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "id", target = "pk")
    @Mapping(target = "image", expression =  "java(ads.getImage() != null ? ads.getImage().getUrl() : \"\")")
    AdDto adsToAdsDto(Ads ads);
    AdsDto listAdsToAdsDto(Integer count, List<Ads> results);
    Ads createOrUpdateAdDtoToAds(CreateOrUpdateAdDto createOrUpdateAdDto);
    @Mapping(target = "description", source = "description")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "image", expression = "java(ads.getImage() != null ? ads.getImage().getUrl() : \"\")")
    @Mapping(target = "pk", source = "id")
    ExtendedAdDto toExtendedAdDto(Ads ads);

    void updateAdsFromCreateOrUpdateAdDto(CreateOrUpdateAdDto createOrUpdateAdDto,  @MappingTarget Ads ads);




    }

