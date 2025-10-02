package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.AdDto;

@Mapper(componentModel = "spring")
public interface AdMapper {

    AdDto toDto(Ad ad);

    Ad toEntity(AdDto adDto);
}
