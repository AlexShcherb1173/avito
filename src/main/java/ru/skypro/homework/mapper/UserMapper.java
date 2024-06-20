package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.security.EncodedMapping;
import ru.skypro.homework.security.PasswordEncoderMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(target = "image", qualifiedByName = "pathToImageEntity")
    UserDto toUserDto(UserEntity user);

    @Mapping(source = "email", target = "username")
    Register toRegister(UserEntity user);

    @Mapping(source = "username", target = "email")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    UserEntity toUser(Register register);

    @Mapping(source = "username", target = "email")
    UserEntity toUser(Login login);

    UpdateUser toUpdateUser(UserEntity user);

    UserEntity toUser(UpdateUser updateUser);

    @Named("pathToImageEntity")
    default String pathToImageEntity(Image image) {
        if (image == null) {
            return null;
        }
        return "/image/" + image.getId();
    }
}
