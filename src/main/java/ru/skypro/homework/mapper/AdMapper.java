package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Users;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "author", source = "author")
    Ad createOrUpdateAdToAdEntity(CreateOrUpdateAd createOrUpdateAd, Users author);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image", qualifiedByName = "imageToString")
    AdDTO adEntityToAdDTO(Ad ad);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "image", target = "image", qualifiedByName = "imageToString")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "title", target = "title")
    ExtendedAd adEntityToExtendedAd(Ad ad);

    @Named("imageToString")
    default String imageToString(Image image) {
        return image != null ? "/images/" + image.getId() : null;
    }
}