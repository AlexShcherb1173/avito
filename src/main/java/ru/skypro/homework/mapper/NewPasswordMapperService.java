package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.entity.UserEntity;

public interface NewPasswordMapperService {
    UserEntity mappingToEntity(NewPasswordDto dto);
}
