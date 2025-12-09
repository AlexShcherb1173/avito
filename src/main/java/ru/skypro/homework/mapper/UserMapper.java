package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Маппер для преобразования между сущностью {@link UserEntity} и DTO.
 * Использует MapStruct для автоматической генерации кода преобразования.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует объект {@link Register} в сущность {@link UserEntity}.
     * Игнорирует поля, которые не должны быть установлены при регистрации.
     *
     * @param register DTO с данными для регистрации
     * @return сущность пользователя
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "email", source = "username")
    UserEntity toEntity(Register register);

    /**
     * Обновляет сущность {@link UserEntity} данными из {@link UpdateUser}.
     * Обновляются только предоставленные поля.
     *
     * @param updateUser DTO с обновляемыми данными
     * @param entity сущность для обновления
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntityFromDto(UpdateUser updateUser, @org.mapstruct.MappingTarget UserEntity entity);

    /**
     * Преобразует сущность {@link UserEntity} в DTO {@link User}.
     * Формирует URL для изображения пользователя.
     *
     * @param entity сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "image", source = "image")
    User toDto(UserEntity entity);

    /**
     * Формирует URL для изображения пользователя.
     * Добавляет временную метку для предотвращения кэширования.
     *
     * @param image имя файла изображения
     * @return строка с URL изображения
     */
//    default String getImageUrl(String image) {
//        if (image == null || image.isEmpty()) {
//            return null;
//        }
//        // Добавляем временную метку для предотвращения кэширования
//        long timestamp = System.currentTimeMillis();
//        return "/images/" + image + "?v=" + timestamp;
//    }
}