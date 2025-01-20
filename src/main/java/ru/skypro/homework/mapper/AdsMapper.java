package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.entity.Ads;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsMapper {

    /**
     * Маппинг объявления в объект AdsDto.
     *
     * @throws NullPointerException если поле ad.image == null.
     */
    @Mapping(source = "author.id", target = "author")
    @Mapping(target = "image", expression = "java(\"/image/\" + ad.getImage().getId())")
    AdsDto toDto(Ads ad);

    /**
     * Маппинг объявления в объект FullAdsDto.
     *
     * @throws NullPointerException если поле ad.image == null.
     */
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(target = "image", expression = "java(\"/image/\" + ad.getImage().getId())")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "author.email", target = "email")
    FullAdsDto toFullAdsDto(Ads ad);

    /**
     * Создание объявления из объекта createAdsDto.
     */
    Ads toAds(CreateAdsDto createAdsDto);

    /**
     * Обновление полей объявления данными из объекта CreateAdsDto.
     */
    void updateAds(CreateAdsDto createAdsDto, @MappingTarget Ads ad);
}