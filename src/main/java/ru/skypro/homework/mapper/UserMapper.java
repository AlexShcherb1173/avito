package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

@Component
public class UserMapper {

    public UserEntity toUserEntity(Register register) {
        if (register == null) {
            throw new NullPointerException("Переданный объект register is null");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(register.getUsername());
        userEntity.setPassword(register.getPassword()); // Надо хешировать
        userEntity.setFirstName(register.getFirstName());
        userEntity.setLastName(register.getLastName());
        userEntity.setPhone(register.getPhone());
        userEntity.setRole(register.getRole());
        // Передача ссылки на аватар - не реализовал, но при регистрации сразу его добавить нельзя
        return userEntity;
    }

    // Маппинг из UserEntity в User DTO
    public User toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("Переданный объект userUntuty is null");
        }
        User userDto = new User();
        userDto.setEmail(userEntity.getUsername()); //в dto называние поля email
        userDto.setFirstName(userEntity.getFirstName()); //имя
        userDto.setLastName(userEntity.getLastName()); //фамилия
        userDto.setPhone(userEntity.getPhone()); //телефон
        userDto.setRole(userEntity.getRole()); //роль
        return userDto;
    }
}
