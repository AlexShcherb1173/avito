package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;
@Mapper
public interface UserDTOMapper {
    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);
    @Mapping(target = "password", ignore = true)
    UserDTO userToAllInfoUserDTO(User user);
    @Mapping(target = "id", source = "id")
    UserDTO userToUserId(User user);
}
