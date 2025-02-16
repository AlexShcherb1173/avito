package ru.skypro.homework.mapper;
//import ru.skypro.homework.model.Image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UserUpdateInfoDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

import java.util.Optional;




/**
 * @author Yuri-73
 */
@Component
@Slf4j
public class UserMapper {
    /**
     * Метод преобразует Dto UserDTO и Dto Register в объект класса User.
     * @param register Dto Register.
     * @return объект класса User.
     */
    @Autowired
    private PasswordEncoder encoder;



    public User registerToUser(RegisterDTO register) {
        if (register == null) {
            throw new IllegalArgumentException("Попытка конвертировать register == null");
        }
        User newUser = new User();
        newUser.setUsername(register.getUsername());
        newUser.setPassword(encoder.encode(register.getPassword()));
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());
        return newUser;
    }

    /**
     * Метод преобразует объект класса User в Dto UserDto.
     *
     * @param user объект класса User.
     * @return Dto UserDto.
     */
    public UserDTO userToUserDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Попытка конвертировать user == null");
        }
        UserDTO userDto = new UserDTO();
        log.info("Попытка преобразовать User в UserDTO для showInfo");
        userDto.setId(user.getId());
        userDto.setEmail(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
//        userDto.setImage("/images/" + user.getImage().getId());
        if (user.getUserAvatar() != null) {
            userDto.setImage("http://localhost:8080/images/" + user.getUserAvatar().getId());
        } else {
            userDto.setImage("http://localhost:8080/images/");
        }
        return userDto;
    }

    /**
     * Метод преобразует объект класса User в Dto UpdateUserDTO.
     * @param user объект класса User.
     * @return Dto UpdateUserDTO.
     */
//    public static UpdateUserDTO updateUserToUserDto(User user) {
//        if (user == null) {
//            throw new IllegalArgumentException("Попытка конвертировать user == null");
//        }
//        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
//
//        updateUserDTO.setFirstName(user.getFirstname());
//        updateUserDTO.setLastName(user.getLastname());
//        updateUserDTO.setPhone(user.getPhone());
//
//        return updateUserDTO;
//    }
}