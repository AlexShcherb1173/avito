package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "email", source = "username")
    public abstract UserEntity toEntity(Register register);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    public abstract void updateEntityFromDto(UpdateUser updateUser, @MappingTarget UserEntity entity);

    @Mapping(target = "image", qualifiedByName = "imageToUrl")
    public abstract User toDto(UserEntity entity);

    @Named("imageToUrl")
    protected String imageToUrl(String image) {
        return image != null && !image.isEmpty() ? "/images/" + image : null;
    }

    // Явные методы для полей, которые НЕ должны преобразовываться
    @Named("identity")
    protected String identity(String value) {
        return value;
    }
}