package ru.skypro.homework.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User userEntity);

    User toUserEntity(UserDto userDto);

    void updateEntityFromDto(UpdateUserDto updateDTO, @MappingTarget User user);

    List<UserDto> toDTOList(List<User> users);

    List<User> toEntityList(List<UserDto> dtos);

}
