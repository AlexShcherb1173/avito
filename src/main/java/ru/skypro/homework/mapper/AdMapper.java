package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Advertisement;
import ru.skypro.homework.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    @Mapping(target = "author", source = "advertisement.author.id")
    @Mapping(target = "pk", source = "advertisement.id")
    @Mapping(target = "image", source = "advertisement.image")  // Добавлено
    Ad advertisementToAd(Advertisement advertisement);

    @Mapping(target = "pk", source = "advertisement.id")
    @Mapping(target = "authorFirstName", source = "advertisement.author.firstName")
    @Mapping(target = "authorLastName", source = "advertisement.author.lastName")
    @Mapping(target = "email", source = "advertisement.author.username")
    @Mapping(target = "phone", source = "advertisement.author.phone")
    @Mapping(target = "image", source = "advertisement.image")  // Добавлено
    ExtendedAd advertisementToExtendedAd(Advertisement advertisement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    Advertisement createOrUpdateAdToAdvertisement(CreateOrUpdateAd createOrUpdateAd, User user);

    List<Ad> advertisementToAdList(List<Advertisement> advertisements);
}
