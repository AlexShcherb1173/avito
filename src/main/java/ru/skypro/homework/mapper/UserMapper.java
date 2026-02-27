package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    User toDto(UserEntity entity);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void applyUpdate(UpdateUser dto, @MappingTarget UserEntity entity);
//}

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//
//    @Mapping(target = "role", expression = "java(entity.getRole().name())") // enum → String
//    @Mapping(target = "avatar", source = "image") // image → avatar
//    User toDto(UserEntity entity);
//
//}

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(entity.getRole().name())")
    @Mapping(target = "image", source = "image")  // ✅ image → image
    User toDto(UserEntity entity);

}