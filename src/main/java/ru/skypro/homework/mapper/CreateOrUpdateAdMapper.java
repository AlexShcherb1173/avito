package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface CreateOrUpdateAdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ad toAd(CreateOrUpdateAd createOrUpdateAd);

}
