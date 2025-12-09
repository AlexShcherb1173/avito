package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

/**
 * Маппер для преобразования между сущностью {@link AdEntity} и DTO объявлений.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface AdMapper {

    /**
     * Преобразует объект {@link CreateOrUpdateAd} в сущность {@link AdEntity}.
     * Игнорирует поля, которые устанавливаются отдельно.
     *
     * @param createOrUpdateAd DTO с данными объявления
     * @return сущность объявления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdEntity toEntity(CreateOrUpdateAd createOrUpdateAd);

    /**
     * Преобразует сущность {@link AdEntity} в DTO {@link Ad}.
     * Формирует URL для изображения объявления.
     *
     * @param entity сущность объявления
     * @return базовое DTO объявления
     */
    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "price", source = "entity.price")
    @Mapping(target = "title", source = "entity.title")
    Ad toDto(AdEntity entity);

    /**
     * Преобразует сущность {@link AdEntity} в расширенное DTO {@link ExtendedAd}.
     * Включает подробную информацию об авторе и объявлении.
     *
     * @param entity сущность объявления
     * @return расширенное DTO объявления
     */
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "authorFirstName", source = "entity.author.firstName")
    @Mapping(target = "authorLastName", source = "entity.author.lastName")
    @Mapping(target = "description", source = "entity.description")
    @Mapping(target = "email", source = "entity.author.email")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "phone", source = "entity.author.phone")
    @Mapping(target = "price", source = "entity.price")
    @Mapping(target = "title", source = "entity.title")
    ExtendedAd toExtendedAd(AdEntity entity);

    /**
     * Обновляет сущность {@link AdEntity} данными из {@link CreateOrUpdateAd}.
     * Обновляются только предоставленные поля.
     *
     * @param createOrUpdateAd DTO с обновляемыми данными
     * @param entity сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntityFromDto(CreateOrUpdateAd createOrUpdateAd, @org.mapstruct.MappingTarget AdEntity entity);

    /**
     * Формирует URL для изображения пользователя.
     * Добавляет временную метку для предотвращения кэширования.
     *
     * @param image имя файла изображения
     * @return строка с URL изображения
     */
    default String getImageUrl(String image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        // Добавляем временную метку для предотвращения кэширования
        long timestamp = System.currentTimeMillis();
        return "/images/" + image + "?v=" + timestamp;
    }
}