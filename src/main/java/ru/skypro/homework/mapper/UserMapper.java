package ru.skypro.homework.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    /**
     * Маппинг пользователя в объект UserDto.
     * <p>
     * Поле image преобразуется из пути в файловой системе в URL-ссылку на изображение.
     * Если поле user.image равно null, в userDto.image также запишется null.
     */
    default UserDto toDto(User user) {

        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());

        Image image = user.getImage();
        if (image != null) {
            userDto.setImage("/image/" + image.getId());
        } else {
            userDto.setImage(null);
        }

        return userDto;
    }

    /**
     * Обновление полей пользователя firstName, lastName и phone данными из UserDto.
     */
    @Mapping(target = "user.image", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateUser(UserDto userDto, @MappingTarget User user);

    /**
     * Обновление поля password в пользователе.
     */
    @Mapping(source = "newPasswordDto.newPassword", target = "password")
    void updateUserPassword(NewPasswordDto newPasswordDto, @MappingTarget User user);

    /**
     * Создание нового пользователя с данными из объекта RegisterReqDto при регистрации.
     */
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "username", target = "email")
    User toUser(RegisterReqDto registerReqDto);

}