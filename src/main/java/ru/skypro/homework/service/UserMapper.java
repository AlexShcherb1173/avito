package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;

@Service
public class UserMapper {

    public User toDto(UserEntity e) {
        if (e == null) return null;
        User dto = new User();
        dto.setId(e.getId().intValue());
        dto.setEmail(e.getUsername());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setPhone(e.getPhone());
        dto.setImage(e.getImage());
        return dto;
    }

    public UserEntity fromRegister(Register r, String encodedPassword) {
        if (r == null) return null;
        return UserEntity.builder()
                .username(r.getUsername())
                .password(encodedPassword)
                .firstName(r.getFirstName())
                .lastName(r.getLastName())
                .phone(r.getPhone())
                .role(r.getRole())
                .build();
    }

    public void updateFrom(UpdateUser req, UserEntity e) {
        if (req == null || e == null) return;
        e.setFirstName(req.getFirstName());
        e.setLastName(req.getLastName());
        e.setPhone(req.getPhone());
    }
}
