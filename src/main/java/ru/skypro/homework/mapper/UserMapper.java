package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.AvatarEntity;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toUpdateUser(UpdateUserDto source);

    @Mapping(target = "email", source = "username")
    UserEntity toUser(RegisterDto source);

    @Mapping(constant = "/users/me/image", target = "image")
    @Mapping(source = "role", target = "role")
    UserDto toUserDto(UserEntity userEntity);

    default String map(AvatarEntity avatar) {
        return avatar != null ? avatar.getPath() : null;
    }


//    @Mapping(expression = "java(buildImageUrl(user.getAvatar().getPath()))", target = "image")
//    @Mapping(source = "role", target = "role")
////    @Mapping(source = "email", target = "email")
////    @Mapping(source = "firstName", target = "firstName")
////    @Mapping(source = "lastName", target = "lastName")
////    @Mapping(source = "phone", target = "phone")
//    UserDto toUserDto(UserEntity user);
//
//    default String map(AvatarEntity avatar) {
//        return avatar != null ? avatar.getPath() : null;
//    }
//
//    default String buildImageUrl(String image) {
//        return "/users/me/image" + image;
//    }


}
