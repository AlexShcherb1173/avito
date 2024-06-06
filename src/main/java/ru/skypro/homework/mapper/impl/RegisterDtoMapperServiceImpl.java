package ru.skypro.homework.mapper.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterDtoMapperService;

@Service
public class RegisterDtoMapperServiceImpl implements RegisterDtoMapperService {
    @Override
    public UserEntity mappingToEntity(RegisterDto dto) {
        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setPhone(dto.getPhone());
        return entity;
    }

    @Override
    public RegisterDto mappingToDto(UserEntity entity) {
        RegisterDto dto = new RegisterDto();
        dto.setUsername(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}
