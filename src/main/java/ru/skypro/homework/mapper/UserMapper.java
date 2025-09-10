package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.enity.User;


@Component
public final class UserMapper {
    private UserMapper() {}

    public static UserDto toDto(User u) {
        if (u == null) return null;

        UserDto dto = new UserDto();
        dto.setId(u.getId() == null ? null : u.getId().intValue());
        dto.setEmail(u.getUsername());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setPhone(u.getPhone());
        dto.setImage(u.getImageUrl() != null ? "/users/" + u.getId() + "/image" : null);
        return dto;
    }


    public static void fillEntity(User target, UserDto dto) {
        if (target == null || dto == null) return;


        target.setUsername(dto.getEmail());
        target.setFirstName(dto.getFirstName());
        target.setLastName(dto.getLastName());
        target.setPhone(dto.getPhone());

    }
}