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
        return userEntity;
    }

    // Маппинг из UserEntity в User DTO
    public User toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("Переданный объект userEntity is null");
        }
        User userDto = new User();
        userDto.setId(userEntity.getId()); // Передача id
        userDto.setEmail(userEntity.getUsername()); // В dto называние поля email
        userDto.setFirstName(userEntity.getFirstName()); // Имя
        userDto.setLastName(userEntity.getLastName()); // Фамилия
        userDto.setPhone(userEntity.getPhone()); // Телефон
        userDto.setRole(userEntity.getRole()); // Роль
        userDto.setImage(userEntity.getImage()); // Ссылка на изображение
        return userDto;
    }
}
