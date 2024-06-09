package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public abstract class AdMapper {
    @Value("${download.url}")
    protected String downloadUrl;

    @Mapping(target = "image", expression = "java(entity.getImage() == null ? \"\" : downloadUrl + entity.getImage().getId())")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    public abstract AdResponse mappingToDto(Ad entity);
}
