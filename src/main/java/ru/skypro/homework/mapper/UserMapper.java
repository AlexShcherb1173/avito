package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.StringUtils;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

/**
 * Mapper for User entity (made using mapstruct)
 * Maps UserDto, UpdateUserDto and RegisterDto
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "image", expression = "java(\"/pictures/\" + user.getImage().getId())")
    UserDto UserToUserDto(User user);

    UpdateUserDto UserToUpdateUserDto(User user);

    default void safeUpdateUserDtoToUserEntity(UpdateUserDto updateUserDto, @MappingTarget User user) {
        if (updateUserDto == null) {
            return;
        }

        if (StringUtils.hasText(updateUserDto.getFirstName())) {
            user.setFirstName(updateUserDto.getFirstName());
        }

        if (StringUtils.hasText(updateUserDto.getLastName())) {
            user.setLastName(updateUserDto.getLastName());
        }

        if (StringUtils.hasText(updateUserDto.getPhone())) {
            user.setPhone(updateUserDto.getPhone());
        }
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "email", source = "username")
    User RegisterDtoToUserEntity(RegisterDto registerDto);
}
