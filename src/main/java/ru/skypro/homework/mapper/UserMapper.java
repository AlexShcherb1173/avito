package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.generatedDto.UserDto;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(source = "avatar", target = "image", qualifiedByName = "getUserAvatarLink")
    UserDto userToUserDto(User user);

    @Named("getUserAvatarLink")
    default String getUserAvatarLink(Avatar avatar) {
        return (avatar == null) ? null : "/users/avatar/" + avatar.getId();
    }
}
