package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

//@Component
//public class UserMapperManual {
//
//    public User toDto(UserEntity entity) {
//        if (entity == null) return null;
//
//        User dto = new User();
//        dto.setId(entity.getId());
//        dto.setEmail(entity.getEmail());
//        dto.setFirstName(entity.getFirstName());
//        dto.setLastName(entity.getLastName());
//        dto.setPhone(entity.getPhone());
//        dto.setImage(entity.getImage());
//
//
//        try {
//            dto.setRole(entity.getRole() == null ? null : Role.valueOf(entity.getRole().name()));
//        } catch (Exception ignored) {
//
//        }
//
//        return dto;
//    }
//
//    public void applyUpdate(UserEntity entity, UpdateUser updateUser) {
//        if (entity == null || updateUser == null) return;
//        if (updateUser.getFirstName() != null) entity.setFirstName(updateUser.getFirstName());
//        if (updateUser.getLastName() != null) entity.setLastName(updateUser.getLastName());
//        if (updateUser.getPhone() != null) entity.setPhone(updateUser.getPhone());
//    }
//}

//@

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Component
public class UserMapperManual {

//    public User toDto(UserEntity entity) {
//        if (entity == null) return null;
//
//        User dto = new User();
//        dto.setId(entity.getId());
//        dto.setEmail(entity.getEmail());
//        dto.setFirstName(entity.getFirstName());
//        dto.setLastName(entity.getLastName());
//        dto.setPhone(entity.getPhone());
//        dto.setAvatar(entity.getImage());
//        dto.setEnabled(true);
//
//        try {
//            dto.setRole(entity.getRole() == null ? "USER" : entity.getRole().name());
//        } catch (Exception ignored) {
//            dto.setRole("USER");
//        }
//
//        return dto;
//    }
//
//    // ✅ ИСПРАВЛЕНО: переименовано в applyUpdate (как в UserService)
//    public void applyUpdate(UpdateUser updateUser, UserEntity entity) {
//        if (entity == null || updateUser == null) return;
//        if (updateUser.getFirstName() != null) entity.setFirstName(updateUser.getFirstName());
//        if (updateUser.getLastName() != null) entity.setLastName(updateUser.getLastName());
//        if (updateUser.getPhone() != null) entity.setPhone(updateUser.getPhone());
//    }

    // ✅ ИСПРАВЛЕНО: переименовано в applyUpdate (как в UserService)
    public void applyUpdate(UpdateUser updateUser, UserEntity entity) {
        if (entity == null || updateUser == null) return;
        if (updateUser.getFirstName() != null) entity.setFirstName(updateUser.getFirstName());
        if (updateUser.getLastName() != null) entity.setLastName(updateUser.getLastName());
        if (updateUser.getPhone() != null) entity.setPhone(updateUser.getPhone());
    }

    public User toDto(UserEntity entity) {
        if (entity == null) return null;

        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());

        // ✅ Формируем правильную ссылку на аватар
        if (entity.getImage() != null && !entity.getImage().isEmpty()) {
            dto.setImage("/" + entity.getImage());  // ← добавили "/"
        } else {
            //dto.setImage("/default-avatar.png");
            dto.setImage("/default-image.png");
        }

        dto.setEnabled(true);

        try {
            dto.setRole(entity.getRole() == null ? "USER" : entity.getRole().name());
        } catch (Exception ignored) {
            dto.setRole("USER");
        }

        return dto;
    }
}
