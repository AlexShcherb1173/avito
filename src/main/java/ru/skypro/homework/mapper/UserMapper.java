package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;

/**
 * Интерфейс UserMapper отвечает за преобразование объектов между
 * сущностью User и различными DTO (Data Transfer Objects) с использованием MapStruct.
 *
 * <p>Данный маппер упрощает преобразование данных между слоями приложения,
 * минимизируя объем повторяющегося кода и обеспечивая более чистую архитектуру.</p>
 *
 * <p>Методы:</p>
 * <ul>
 *     <li>{@link #userToUserDto(User)} - преобразует объект User в объект UserDTO.</li>
 *     <li>{@link #userDtoToUser(UserDTO)} - преобразует объект UserDTO в объект User.</li>
 *     <li>{@link #registerToUser(Register)} - преобразует объект Register в объект User.</li>
 *     <li>{@link #avatarToString(Avatar)} - преобразует объект Avatar в строку,
 *     представляющую URL изображения.</li>
 * </ul>
 *
 * <p>Примечания:</p>
 * <ul>
 *     <li>При преобразовании из UserDTO в User игнорируются поля
 *     {@code password}, {@code avatar} и {@code role}.</li>
 *     <li>При преобразовании из Register в User поле {@code username}
 *     сопоставляется с полем {@code email}, а роль по умолчанию устанавливается на "USER".</li>
 *     <li>Метод {@code avatarToString} возвращает строку по адресу изображения
 *     или {@code null}, если аватар отсутствует.</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    String address = "/users/image/";

    @Mapping(target = "image", source = "avatar", qualifiedByName = "avatarToString")
    UserDTO userToUserDto(User entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "role", ignore = true)
    User userDtoToUser(UserDTO dto);

    @Mapping(target = "role", defaultValue = "USER")
    @Mapping(source = "username", target = "email")
    User registerToUser(Register dto);

    @Named("avatarToString")
    default String avatarToString(Avatar avatar) {
        if (avatar == null) {
            return null;
        }
        return address + avatar.getId();
    }
}
