package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.User;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "image", ignore = true)
    UserDTO userToUserDto(User user);

    @Mapping(target = "image", ignore = true)
    User UserDtoToUser(UserDTO userDTO);
}
