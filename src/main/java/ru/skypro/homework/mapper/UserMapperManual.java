package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Component
public class UserMapperManual {

    public User toDto(UserEntity entity) {
        if (entity == null) return null;

        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setImage(entity.getImage());


        try {
            dto.setRole(entity.getRole() == null ? null : Role.valueOf(entity.getRole().name()));
        } catch (Exception ignored) {

        }

        return dto;
    }

    public void applyUpdate(UserEntity entity, UpdateUser updateUser) {
        if (entity == null || updateUser == null) return;
        if (updateUser.getFirstName() != null) entity.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null) entity.setLastName(updateUser.getLastName());
        if (updateUser.getPhone() != null) entity.setPhone(updateUser.getPhone());
    }
}

