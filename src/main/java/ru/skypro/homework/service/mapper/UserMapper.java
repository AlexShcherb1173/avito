package ru.skypro.homework.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );


    public UserDTO userDTO(User user);
    public User user(UserDTO userDTO);
}
