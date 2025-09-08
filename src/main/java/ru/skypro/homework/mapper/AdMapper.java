package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AdMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "imagePath", qualifiedByName = "imagePathToImage")
    Ad toDto(AdEntity adEntity);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", source = "imagePath", qualifiedByName = "imagePathToImage")
    ExtendedAd toExtendedAd(AdEntity adEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateEntityFromDto(CreateOrUpdateAd createOrUpdateAd, @org.mapstruct.MappingTarget AdEntity adEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd, UserEntity author);

    @org.mapstruct.Named("imagePathToImage")
    default String imagePathToImage(String imagePath) {
        return imagePath != null ? "/ads/image/" + imagePath : null;
    }
}
