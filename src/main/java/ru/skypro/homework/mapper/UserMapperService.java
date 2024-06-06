package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.UserEntity;

public interface UserMapperService {
    UserDto mappingToDto(UserEntity entity);

    UserEntity mappingToEntity(UserDto dto);
}
