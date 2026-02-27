package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ru.skypro.homework.dto.User toDto(ru.skypro.homework.entity.User entity);

    ru.skypro.homework.entity.User toEntity(ru.skypro.homework.dto.User dto);
}