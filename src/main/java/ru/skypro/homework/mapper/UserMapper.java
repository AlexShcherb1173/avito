package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserEntity toUser(RegisterDto registerReqDto);

    @Mapping(source = "username", target = "email")
    @Mapping(target = "image", expression = "java(getImage(user))")
    void toUserDto(@MappingTarget UserDto userDto, UserEntity user);

    default String getImage(UserEntity user) {
        if (user.getImage() == null) {
            return null;
        }
        return "/users/image/" + user.getId() + "/from-db";
    }

    @Mapping(ignore = true, target = "user.id")
    @Mapping(ignore = true, target = "user.image")
    @Mapping(ignore = true, target = "user.username")
    void toUser(@MappingTarget UserEntity user, UserDto userDto);


}
