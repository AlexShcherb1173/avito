package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.rq.user.Register;
import ru.skypro.homework.dto.rq.user.UpdateUser;
import ru.skypro.homework.dto.rs.user.User;
import ru.skypro.homework.entity.RoleEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    UserRepository userRepository;
    @Mapping(target = "role", expression = "java(getEnumRole(userEntity))")
    public abstract User userEntityToUser(UserEntity userEntity);

    public abstract UserEntity updateUserToEntity(UpdateUser updateUser);

    public abstract UpdateUser entityToUpdateUser(UserEntity userEntity);
    @Mapping(target = "role", expression = "java(getEnumRole(userEntity))")
    public abstract Register userEntityToRegister(UserEntity userEntity);

    public abstract UserEntity registerToUserEntity(Register register);

    @Mapping(target = "name", expression = "java(role.name())") //target - переменная объекта в которую мы мапим (expression - выражение, результат которого мы присвоим в target)
   public abstract RoleEntity roleToRoleEntity(Role role);

    public Role getEnumRole(UserEntity userEntity){
        return Role.valueOf(userEntity.getRole().getName());
    }




}
