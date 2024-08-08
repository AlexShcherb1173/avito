package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "username")
    User registerDTOToUser(RegisterDto registerDTO);

    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/image/\" + user.getImage().getId() : null)")
    UserDto toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "adverts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateUserDTOToUser(UpdateUserDto updateUserDTO, @MappingTarget User user);

    UpdateUserDto userToUpdateUserDTO(User user);
}
