package ru.skypro.homework.mapper;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.config.ApplicationConfig;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserApiDto;
import ru.skypro.homework.dto.UserAuthenticationDto;
import ru.skypro.homework.model.User;
/**
 * Класс для преобразования (маппинга) между сущностями и DTO - пользователи.
 */
public class UserMapper {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    public static UserApiDto toApi(User user) {
        UserApiDto userApiDto = new UserApiDto();
        userApiDto.setId(user.getId());
        userApiDto.setEmail(user.getUsername());
        userApiDto.setFirstName(user.getFirstName());
        userApiDto.setLastName(user.getLastName());
        userApiDto.setPhone(user.getPhone());
        userApiDto.setRole(user.getRole());
        if (user.getAvatar() != null) {
            userApiDto.setImage(ApplicationConfig.getPathToAvatars() + user.getAvatar());
        }
        return userApiDto;
    }

    public static UserDetails toUserDetails(User user) {
        return new UserAuthenticationDto(user.getUsername(), user.getPassword(), user.getRole(), user.isEnabled());
    }
    public static UserDetails toUserDetails(RegisterDto user) {
        return new UserAuthenticationDto(user.getUsername(), user.getPassword(), user.getRole(), 1);
    }

    public static User toEncodedUser(RegisterDto registerDto) {

        return new User(null, registerDto.getUsername(), registerDto.getFirstName(), registerDto.getLastName(), registerDto.getPhone(), registerDto.getRole()
                , passwordEncoder.encode(registerDto.getPassword()), 1, null);
    }

    public static User toUser(UserDetails user) {
        return new User(null, user.getUsername(), null, null, null, Role.USER, user.getPassword(), user.isEnabled() ? 1 : 0, null);
    }
}
