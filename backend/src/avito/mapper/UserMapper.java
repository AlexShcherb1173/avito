package ru.avito.mapper;

import org.springframework.stereotype.Component;
import ru.avito.dto.user.UserDto;
import ru.avito.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole().name(),
                user.getImage()
        );
    }
}