package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(
        componentModel = "spring",
        uses = UserMapper.class
)
public abstract class AdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToUrl")
    @Mapping(target = "pk", source = "id")
    public abstract Ad toDto(AdEntity entity);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")      // Добавить
    @Mapping(target = "authorLastName", source = "author.lastName")        // Добавить
    @Mapping(target = "description", source = "description")               // Добавить если нет
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToUrl")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    public abstract ExtendedAd toExtendedAd(AdEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract void updateEntityFromDto(CreateOrUpdateAd createOrUpdateAd,
                                             @MappingTarget AdEntity entity);
}