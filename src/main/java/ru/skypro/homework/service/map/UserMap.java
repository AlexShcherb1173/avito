package ru.skypro.homework.service.map;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

@Component
public class UserMap {
    /**
     * Метод преобразует объект класса User в Dto UserDto
     * @param user объект класса User
     * @return Dto UserDto
     */
    public UserDto mapUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(userDto.getId());
        userDto.setEmail(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        userDto.setImage("/users/me/image");
        return userDto;
    }

    public User updateUser(User user, UpdateUserDto updateUserDto) {
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());
        return user;
    }

    public UpdateUserDto mapUpdateUserDto(User user) {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName(user.getFirstName());
        updateUserDto.setLastName(user.getLastName());
        updateUserDto.setPhone(user.getPhone());
        return updateUserDto;
    }

    /**
     * Метод преобразует Dto Register в объект класса User.
     * @param register dto Register
     * @return объект класса User
     */
    public User registerToUser(Register register) {
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(register.getPassword());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());
        return user;
    }

}
