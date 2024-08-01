package ru.skypro.homework.mapper;
/*маппер для регистрациию*/
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.autoAndReg.Register;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public abstract class RegisterMapper {
    public abstract Register toDto(User user);

    @Mapping(target = "email", source = "username")
    public abstract User toEntity(Register userDto);
}
