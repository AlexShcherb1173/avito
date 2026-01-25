package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.model.Register;
import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity fromRegister(Register register);

    void updateFromDto(UpdateUser dto, @MappingTarget UserEntity entity);

    @Mapping(source = "role", target = "role")
    User toDto(UserEntity entity);
}
