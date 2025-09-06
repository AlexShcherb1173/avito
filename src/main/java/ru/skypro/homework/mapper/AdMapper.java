package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.responseDto.AdDto;
import ru.skypro.homework.responseDto.ExtendedAdDto;

import java.util.List;

// Маппер для преобразования между сущностью Ad и DTO.
// Обеспечивает корректное отображение полей, включая обработку URL изображений.

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    // Преобразует сущность Ad в AdDto.
    // @param ad сущность объявления
    // @return AdDto

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapImageUrl")
    AdDto toAdDto(Ad ad);

    // Преобразует сущность Ad в ExtendedAdDto.
    // Маппит поля автора: имя, фамилия, email, телефон.
    // @param ad сущность объявления
    // @return ExtendedAdDto

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.username", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    @Mapping(source = "image", target = "image", qualifiedByName = "mapImageUrl")
    ExtendedAdDto toExtendedAdDto(Ad ad);

    // Преобразует CreateOrUpdateAd в Ad.
    // Игнорирует поля, которые должны устанавливаться в сервисе.
    // @param dto DTO для создания/обновления
    // @return Ad

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "image", ignore = true)
    Ad toAd(CreateOrUpdateAd dto);

    // реобразует список объявлений в список AdDto.
    // @param ads список сущностей
    // @return список DTO

    List<AdDto> toAdDtoList(List<Ad> ads);

    // Преобразует список объявлений в список ExtendedAdDto.
    // @param ads список сущностей
    // @return список DTO

    List<ExtendedAdDto> toExtendedAdDtoList(List<Ad> ads);

    // Преобразует путь изображения в полный URL.
    // Если изображение уже содержит полный путь, возвращает как есть.
    // Если изображение содержит только имя файла, добавляет префикс пути.
    // @param image путь или имя файла изображения
    // @return полный URL изображения

    @Named("mapImageUrl")
    default String mapImageUrl(String image) {
        if (image == null || image.isBlank()) {
            return null;
        }

        // Если уже полный URL (начинается с /images/), возвращаем как есть
        if (image.startsWith("/images/")) {
            return image;
        }

        // Если это просто имя файла, добавляем префикс пути к изображениям объявлений
        return "/images/ads/" + image;
    }
}