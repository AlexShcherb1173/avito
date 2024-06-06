package ru.skypro.homework.mapper.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UpdateUserDtoMapperService;
@Service
public class UpdateUserDtoMapperServiceImpl implements UpdateUserDtoMapperService {
    @Override
    public UserEntity mappingToEntity(UpdateUserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        return entity;
    }

    @Override
    public UpdateUserDto mappingToDto(UserEntity entity) {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setFirstName(dto.getFirstName());
        dto.setLastName(dto.getLastName());
        dto.setPhone(dto.getPhone());
        return dto;
    }
}
