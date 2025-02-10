package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.Register;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.model.User;

/**
 * @author Archinski
 */
@Component
public class UserMapper {
    /**
     * Метод преобразует Dto UserDTO и Dto Register в объект класса User.
     *
     * @param register Dto Register.
     * @param userDTO Dto UserDTO.
     * @return объект класса User.
     */

    public User registerToUser(Register register, UserDTO userDTO) {
        User newUser = new User();
        newUser.setUsername(register.getUsername());
        newUser.setPassword(register.getPassword());
        newUser.setFirstname(register.getFirstName());
        newUser.setLastname(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());
        newUser.setImage(userDTO.getImage().getBytes());
        return newUser;
    }

    /**
     * Метод преобразует объект класса User в Dto UserDto.
     * @param user объект класса User.
     * @return Dto UserDto.
     */
    public UserDTO userToUserDto(User user) {
        UserDTO userDto = new UserDTO();

        userDto.setId(user.getId());
        userDto.setEmail(user.getPassword());
        userDto.setFirstName(user.getFirstname());
        userDto.setLastName(user.getLastname());
        userDto.setPhone(user.getPhone());
        userDto.setRole(String.valueOf(user.getRole()));
        userDto.setImage(user.getImage().toString());

        return userDto;
    }
}
