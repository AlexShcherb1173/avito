package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

/**
 * Интерфейс, использующий библиотеку MapStruct для автоматического преобразования между объектами типа {@link User} и {@link UserDTO}.
 * Этот интерфейс выполняет маппинг данных пользователя, таких как изображение профиля, а также преобразует объект {@link UserDTO} в {@link User}.
 * MapStruct автоматически генерирует реализацию этого интерфейса для эффективного преобразования между объектами.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", expression = "java(entity.getImage() != null ? \"/images/\" + entity.getImage() : null)")
    UserDTO userToUserDTO(User entity);

    User userDTOToUser(UserDTO userDTO);
}