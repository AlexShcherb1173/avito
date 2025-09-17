package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

@Component
public class UserMapper {

    public UserDto toUserDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    public User toUserEntity(Register register) {

        User entity = new User();
        entity.setUsername(register.getUsername());
        entity.setPassword(register.getPassword());
        entity.setRole(register.getRole());
        entity.setEmail(register.getEmail());
        entity.setFirstName(register.getFirstName());
        entity.setLastName(register.getLastName());
        return entity;
    }

}
