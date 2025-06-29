package ru.skypro.homework.service.impl.UserService;


import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Registration.Register;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.User;


@Component

public class UserMapper {

    public UserDTO toDto(User user) {
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

    public User toEntity(Register registerDto) {
        return User.builder()
                .email(registerDto.getUsername())
                .password(registerDto.getPassword())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .phone(registerDto.getPhone())
                .role(registerDto.getRole())
                .build();
    }

}
