package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

/**
 * Маппер для преобразования между сущностью Ad и DTO объектами.
 * Обеспечивает конвертацию данных между слоем базы данных и API.
 */
public class AdMapper {

    /**
     * Преобразует сущность Ad в DTO объект.
     *
     * @param entity сущность объявления из базы данных
     * @return DTO объект для передачи клиенту
     */
    public static AdDto toDto(Ad entity) {
        AdDto dto = new AdDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    /**
     * Преобразует DTO объект в сущность Ad.
     *
     * @param dto DTO объект с данными от клиента
     * @param author сущность пользователя-автора объявления
     * @return сущность объявления для сохранения в базу данных
     */
    public static Ad toEntity(CreateOrUpdateAdDto dto, User author) {
        Ad entity = new Ad();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setAuthor(author);
        return entity;
    }
}