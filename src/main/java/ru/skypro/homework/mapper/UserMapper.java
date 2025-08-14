package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    @Mapping(target = "role", defaultValue = "USER")
    Users registerToUser(Register register);

    @Mapping(source = "email", target = "email")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? \"/users/image/\" + getFileNameFromPath(user.getImage().getFilePath()) : null)")
    User userEntityToUserDTO(Users user);

    default String getFileNameFromPath(String filePath) {
        if (filePath == null) return null;
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }
}