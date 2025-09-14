package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;

/**
 * Маппер для преобразования между {@link AdEntity} и DTO объявления.
 */
@Mapper(componentModel = "spring")
public interface AdMapper {

    /**
     * Преобразует {@link AdEntity} в краткое DTO {@link Ad}.
     *
     * @param entity сущность объявления
     * @return DTO с краткой информацией об объявлении
     */
    @Mappings({
            @Mapping(target = "author", source = "author.id"),
            @Mapping(target = "pk", source = "id")
    })
    Ad toAdDto(AdEntity entity);

    /**
     * Преобразует {@link AdEntity} в расширенное DTO {@link ExtendedAd}.
     *
     * @param entity сущность объявления
     * @return DTO с полной информацией об объявлении
     */
    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "authorFirstName", source = "author.firstName"),
            @Mapping(target = "authorLastName", source = "author.lastName"),
            @Mapping(target = "email", source = "author.email"),
            @Mapping(target = "phone", source = "author.phone")
    })
    ExtendedAd toExtendedDto(AdEntity entity);

    /**
     * Создаёт новую сущность {@link AdEntity} из DTO {@link CreateOrUpdateAd}.
     * Игнорирует поля id, author и comments.
     *
     * @param dto DTO с данными для создания объявления
     * @return новая сущность объявления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity fromCreateDto(CreateOrUpdateAd dto);

    /**
     * Обновляет существующую сущность {@link AdEntity} на основе DTO {@link CreateOrUpdateAd}.
     * Поля с null в DTO не переносятся в сущность.
     *
     * @param dto    DTO с обновлёнными данными
     * @param entity сущность, которую нужно обновить
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CreateOrUpdateAd dto, @MappingTarget AdEntity entity);
}
