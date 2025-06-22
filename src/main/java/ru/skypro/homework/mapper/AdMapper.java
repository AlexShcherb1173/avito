package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.model.Ad;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {

    /*
     Преобразование Ad в AdDTO
     */
    AdDto toDto(Ad ad);

    /*
     Преобразование AdDTO в Ad
     */
    Ad toEntity(AdDto dto);

    /*
     Преобразование списка Ad в список AdDTO
     */
    List<AdDto> toDtoList(List<Ad> ads);

    /*
     Преобразование списка AdDTO в список Ad
     */
    List<Ad> toEntityList(List<AdDto> dtos);
}
