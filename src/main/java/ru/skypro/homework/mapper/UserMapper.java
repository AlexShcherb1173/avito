package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.register.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;

@Component
public class UserMapper {

    //Преобразование сущности в DTO пользователя

    public User toUserDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setPhone(userEntity.getPhoneNumber());
        user.setImage(userEntity.getImagePath());
        return user;
    }

    //Преобразование DTO обновления в сущность

    public void updateUserEntityFromDto(UserEntity userEntity, UpdateUser updateUser) {
        if (userEntity == null || updateUser == null) {
            return;
        }

        userEntity.setFirstName(updateUser.getFirstName());
        userEntity.setLastName(updateUser.getLastName());
        userEntity.setPhoneNumber(updateUser.getPhone());
    }
} 