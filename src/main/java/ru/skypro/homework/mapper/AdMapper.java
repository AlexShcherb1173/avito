
package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "productImg", source = "image")
    })


    Ad toAdDto(AdEntity entity);

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "authorLastName", source = "author.lastName"),
            @Mapping(target = "email", source = "author.email"),
            @Mapping(target = "phone", source = "author.phone"),
            @Mapping(target = "productImg", source = "image")
    })


    ExtendedAd toExtendedDto(AdEntity entity);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "image", ignore = true),
            @Mapping(target = "comments", ignore = true)
    })
    void applyCreateOrUpdate(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);
}

