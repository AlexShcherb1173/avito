package ru.skypro.homework.mappers;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.models.UserEntity;
@Service
public class UserMapper {
    public UserEntity toUserEntity(RegisterDto userDto) {
        UserEntity user = new UserEntity();
        user.setEmail(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        return user;
    }

    public UserDto toUserDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole());
        dto.setImage(entity.getImage());
        return dto;
    }

    public UpdateUserDto toUpdateUser(UpdateUserDto dto,UserEntity user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        return dto;
    }
}
