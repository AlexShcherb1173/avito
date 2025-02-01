package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Advertisement;

@Mapper(componentModel = "spring")
public interface AdMapper {
    public default Ad toDto(Advertisement ad) {
        return new Ad();
    }

    public default Advertisement toEntity(CreateOrUpdateAd adDto) {
        return new Advertisement();
    }
}
