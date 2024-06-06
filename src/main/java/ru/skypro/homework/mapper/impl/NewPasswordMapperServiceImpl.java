package ru.skypro.homework.mapper.impl;

import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.NewPasswordMapperService;

public class NewPasswordMapperServiceImpl implements NewPasswordMapperService {
    @Override
    public UserEntity mappingToEntity(NewPasswordDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(dto.getNewPassword());
        return userEntity;
    }
}
