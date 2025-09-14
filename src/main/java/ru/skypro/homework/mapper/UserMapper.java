package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

/**
 * Маппер для преобразования между {@link UserEntity} и DTO пользователя.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует сущность {@link UserEntity} в DTO {@link User}.
     *
     * @param entity пользователь из БД
     * @return DTO с данными пользователя
     */
    User toDto(UserEntity entity);

    /**
     * Обновляет поля сущности {@link UserEntity} на основе {@link UpdateUser}.
     * Игнорирует null-значения в DTO.
     *
     * @param dto    DTO с обновляемыми данными
     * @param entity сущность пользователя, в которую вносятся изменения
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateUser dto, @MappingTarget UserEntity entity);
}
