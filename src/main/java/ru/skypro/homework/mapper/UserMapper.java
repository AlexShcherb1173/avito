package ru.skypro.homework.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.model.User;
@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)//
public interface UserMapper {
    @Mapping(source = "image", target = "image.id")
    User toModel(UserDTO dto);

    UpdateUserDTO toDtoUpdateUserDTO(User user);
    @Mapping(target = "image", source = "image.id")
    UserDTO toDtoUserDTO(User user);
}

