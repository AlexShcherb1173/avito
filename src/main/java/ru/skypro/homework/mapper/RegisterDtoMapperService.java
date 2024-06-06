package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.UserEntity;

public interface RegisterDtoMapperService {
    UserEntity mappingToEntity(RegisterDto dto);

    RegisterDto mappingToDto(UserEntity entity);
}
