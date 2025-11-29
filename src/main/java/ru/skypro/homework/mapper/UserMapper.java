package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "email", source = "username")
    UserEntity toEntity(Register register);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntityFromDto(UpdateUser updateUser, @org.mapstruct.MappingTarget UserEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "image", source = "image")
    User toDto(UserEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "email", source = "entity.email")
    @Mapping(target = "firstName", source = "updateUser.firstName")
    @Mapping(target = "lastName", source = "updateUser.lastName")
    @Mapping(target = "phone", source = "updateUser.phone")
    @Mapping(target = "role", source = "entity.role")
    @Mapping(target = "image", source = "entity.image")
    User toDtoFromUpdate(UserEntity entity, UpdateUser updateUser);
}