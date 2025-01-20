package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AdsMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsListMapper {

    /**
     * Маппинг списка объявлений в список объектов AdsDto.
     */
    List<AdsDto> toDto(List<Ads> adsList);

    /**
     * Маппинг списка объявлений в объект ResponseWrapperAdsDto,
     * содержащий сам список и поле с количеством записей в нём.
     */
    default ResponseWrapperAdsDto toResponseWrapperAdsDto(List<Ads> adsList) {
        ResponseWrapperAdsDto dto = new ResponseWrapperAdsDto();
        dto.setCount(adsList.size());
        dto.setResults(toDto(adsList));
        return dto;
    }
}