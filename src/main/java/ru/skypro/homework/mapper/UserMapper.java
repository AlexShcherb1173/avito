package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.model.User;

/**
 * @author Archinski
 */
@Component
public class UserMapper {
    /**
     * Метод преобразует Dto UserDTO и Dto Register в объект класса User.
     * @param register Dto Register.
     * @return объект класса User.
     */

    public static User registerToUser(Register register) {
        if (register == null) {
            throw new IllegalArgumentException("Попытка конвертировать register == null");
        }
        User newUser = new User();
        newUser.setUsername(register.getUsername());
        newUser.setPassword(register.getPassword());
        newUser.setFirstname(register.getFirstName());
        newUser.setLastname(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());
        return newUser;
    }

    /**
     * Метод преобразует объект класса User в Dto UserDto.
     * @param user объект класса User.
     * @return Dto UserDto.
     */
    public UserDTO userToUserDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Попытка конвертировать user == null");
        }
        UserDTO userDto = new UserDTO();

        userDto.setId(user.getId());
        userDto.setEmail(user.getUsername());
        userDto.setFirstName(user.getFirstname());
        userDto.setLastName(user.getLastname());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        userDto.setImage("src/main/java/ru/skypro/homework/images");

        return userDto;
    }

    /**
     * Метод преобразует объект класса User в Dto UpdateUserDTO.
     * @param user объект класса User.
     * @return Dto UpdateUserDTO.
     */
    public static UpdateUserDTO updateUserToUserDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Попытка конвертировать user == null");
        }
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        updateUserDTO.setFirstName(user.getFirstname());
        updateUserDTO.setLastName(user.getLastname());
        updateUserDTO.setPhone(user.getPhone());

        return updateUserDTO;
    }
}
