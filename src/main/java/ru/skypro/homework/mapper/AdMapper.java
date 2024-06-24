package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorToInt")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    AdDTO modelToAdDTO(Ad ad);

    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", source = "image", qualifiedByName = "imageToString")
    ExtendedAdDTO modelToExtendedAdDTO(Ad ad);

    Ad createOrUpdateAdDTOToAd(CreateOrUpdateAdDTO createOrUpdateAdDTO);

    @Named("authorToInt")
    default Integer authorToInt(User author) {
        return author.getId();
    }

    @Named("imageToString")
    default String imageToString(Image image) {
        return "/image/" + image.getId();
    }
}
