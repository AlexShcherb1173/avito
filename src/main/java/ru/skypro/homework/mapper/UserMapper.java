package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDtoToUser(UserDto userDto);

    @Mapping(source = "email", target = "email")
    @Mapping(expression = "java(getUrlToImageCE(user))", target = "image")
    UserDto userToUserDto(User user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", expression = "java(user.getId())")
    void updateUser(UserDto userDto, @MappingTarget User user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(RegisterDto registerDto, @MappingTarget User user);

    default String getUrlToImage(User user) {
        if (user.getImage() == null){
            return null;
        }
        return "/users/me/image";
    }
}
