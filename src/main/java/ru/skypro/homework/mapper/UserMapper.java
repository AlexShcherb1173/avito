package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

public class UserMapper {

    public User toDto(UserEntity entity) {
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setImage(entity.getImage());

        return dto;
    }


    public UserEntity toEntity(User dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setImage(dto.getImage());

        return entity;
    }
}


