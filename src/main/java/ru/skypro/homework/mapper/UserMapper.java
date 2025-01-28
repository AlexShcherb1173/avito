package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

@Service
public class UserMapper {


    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRoleDto(user.getRoleDto());
        return userDto;
    }

    public User toUsers(RegisterDto registerDto) {
        if (registerDto == null) {
            return null;
        }
        User users = new User();
        users.setEmail(registerDto.getUsername());
        users.setPassword(registerDto.getPassword());
        users.setFirstName(registerDto.getFirstName());
        users.setLastName(registerDto.getLastName());
        users.setPhone(registerDto.getPhone());
        users.setRoleDto(registerDto.getRole());

        return users;
    }
//    public User toRegisterUser(RegisterDto registerDTO) {
//        User user = new User();
//        user.setEmail(registerDTO.getUsername());
//        user.setPassword(registerDTO.getPassword());
//        user.setFirstName(registerDTO.getFirstName());
//        user.setLastName(registerDTO.getLastName());
//        user.setPhone(registerDTO.getPhone());
//        user.setRoleDto(registerDTO.getRole());
//        return user;
//    }
}
