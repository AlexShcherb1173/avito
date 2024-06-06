package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.UserEntity;

public interface UpdateUserDtoMapperService {
    UserEntity mappingToEntity(UpdateUserDto dto);

    UpdateUserDto mappingToDto(UserEntity entity);
}
