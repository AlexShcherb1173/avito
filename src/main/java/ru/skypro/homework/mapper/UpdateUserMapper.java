package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public abstract class UpdateUserMapper {

    public abstract UpdateUser fromUser(User register);

}
