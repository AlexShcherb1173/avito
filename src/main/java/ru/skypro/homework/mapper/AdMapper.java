package ru.skypro.homework.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;

import java.util.List;

@Mapper(componentModel = "spring")
    public interface AdMapper {

        //_____ toModel___
        @Mapping(source = "author", target = "author.id")
        @Mapping(source = "image", target = "image.id")
        @Mapping(source = "pk", target = "id")
        @Mapping(source = "title", target = "title")
        @Mapping(source = "price", target = "price")
        Ad toModel(AdDTO dto);

        @Mapping(source = "title", target = "title")
        Ad toModel(CreateOrUpdateAdDTO dto);

        //
        @Mapping(source = "pk", target = "id")
        @Mapping(source = "authorFirstName", target = "author.firstName")
        @Mapping(source = "authorLastName", target = "author.lastName")
        @Mapping(source = "email", target = "author.email")
        @Mapping(source = "image", target = "image.id")
        @Mapping(source = "phone", target = "author.phone")
        @Mapping(source = "title", target = "title")
        @Mapping(source = "price", target = "price")
        Ad toModel(ExtendedAdDTO dto);

        //
        //_____ toDto___
        @Mapping(target = "author", source = "author.id")
        @Mapping(target = "image", source = "image.id")
        @Mapping(target = "pk", source = "id")
        @Mapping(target = "title", source = "title")
        AdDTO toDtoAdDTO(Ad ad);

        CreateOrUpdateAdDTO toDtoCreateOrUpdateAdDTO(Ad ad);

        @Mapping(target = "pk", source = "id")
        @Mapping(target = "authorFirstName", source = "author.firstName")
        @Mapping(target = "authorLastName", source = "author.lastName")
        @Mapping(target = "email", source = "author.email")
        @Mapping(target = "image", source = "image.id")
        @Mapping(target = "phone", source = "author.phone")
        @Mapping(target = "title", source = "title")
        ExtendedAdDTO toDtoExtendedAdDTO(Ad ad);



        @Mapping(target = "count", source = "size")
        @Mapping(target = "results", source = "list")
        AdsDTO toAds(Integer size, List<Ad> list);

    }
