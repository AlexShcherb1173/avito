package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

public class UserMapper {


    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRoleDto());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        User user=new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRoleDto(userDto.getRole());
        return user;
    }
}
