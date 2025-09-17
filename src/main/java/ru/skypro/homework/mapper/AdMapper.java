package ru.skypro.homework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    
    Ad toFullEntity(AdResponseDto adResponseDto);
    AdResponseDto toFullDto(Ad ad);

    Ad toEntity(AdDto adDto);
    AdDto toShortDto(Ad ad);

    Ad toEntity(AdRequestDto adRequestDto);
    AdRequestDto toRequestDto(Ad ad);

    default AdsDto toAdsDto(List<Ad> ads) {
        return new AdsDto(ads.size(), ads.stream().map(this::toShortDto).toList());
    }
}
