package ru.skypro.homework.service.Mapper;


import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Registration.Register;
import ru.skypro.homework.dto.User.UpdatedUser;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;


@Component

public class UserMapper {

    public UserDTO toDto(UserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .image(user.getImage())
                .build();
    }

    public UserEntity toEntity(Register registerDto) {
        return UserEntity.builder()
                .email(registerDto.getUsername())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .phone(registerDto.getPhone())
                .role(registerDto.getRole())
                .password(registerDto.getPassword()) // будет закодирован в сервисе
                .build();
    }

    // Сокращающий метод для UserDTO
    public UpdatedUser toUpdatedUser(UserEntity user) {
        return new UpdatedUser(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );
    }
}
