package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.UserEntity;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FileStorageConfig fileStorageConfig;

    public UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());

        if (entity.getRole() != null) {
            dto.setRole(entity.getRole().name());
        }

        // Формируем URL для аватара формата /users/image/{id}
        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            // URL указывает на эндпоинт текущего пользователя
            dto.setImage(fileStorageConfig.getBaseUrl() + "/users/image/" + entity.getId());
        } else {
            dto.setImage(null);
        }
        return dto;
    }

    public UserEntity toEntity(Register dto) {
        if (dto == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getUsername()); // username в Register -> email в Entity
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(Role.valueOf(dto.getRole()));

        return entity;
    }

    public void updateEntityFromDto(UpdateUserDto dto, UserEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
    }

}
