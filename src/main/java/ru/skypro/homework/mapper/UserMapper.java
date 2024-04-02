package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * User to UserDto.
     *
     * @param user
     * @return UserDto
     */
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "image", expression = "java(user.getAvatar() != null ? user.getAvatar().getUrl() : \"\")")
    UserDto userToUserDto(User user);

    /**
     * UserDro to User.
     *
     * @param userDto
     * @return User
     */
    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "firstName", source = "userDto.firstName")
    @Mapping(target = "lastName", source = "userDto.lastName")
    @Mapping(target = "phone", source = "userDto.phone")
    @Mapping(target = "role", source = "userDto.role")
    User userDtoToUser(UserDto userDto);

    /**
     * User to UpdateUserDto.
     *
     * @param user
     * @return UpdateUserDto
     */
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    UpdateUserDto userToUpdateUserDto(User user);

    /**
     * UserDro to User.
     *
     * @param updateUserDto
     * @return User
     */
    @Mapping(target = "firstName", source = "updateUserDto.firstName")
    @Mapping(target = "lastName", source = "updateUserDto.lastName")
    @Mapping(target = "phone", source = "updateUserDto.phone")
    User updateUserDtoToUser(UpdateUserDto updateUserDto);

    /**
     * User to UpdateUserDto.
     *
     * @param user
     * @return UpdateUserDto
     */
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "username", source = "user.email")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "role", source = "user.role")
    Register userToRegister(User user);

    /**
     * UserDro to User.
     *
     * @param register
     * @return User
     */
    @Mapping(target = "firstName", source = "register.firstName")
    @Mapping(target = "lastName", source = "register.lastName")
    @Mapping(target = "phone", source = "register.phone")
    @Mapping(target = "email", source = "register.username")
    @Mapping(target = "password", source = "register.password")
    @Mapping(target = "role", source = "register.role")
    User registerToUser(Register register);

}
