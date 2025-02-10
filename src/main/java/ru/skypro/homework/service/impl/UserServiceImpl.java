package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.exception.IncorrectPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    /**
     * Метод для смены пароля пользователя.
     * Кодировка нового пароля пользователя с помощью бина PasswordEncoder.
     *
     * @param newPassword Dto NewPassword.
     * @param principal интерфейс для получения username пользователя.
     * @throws UserNotFoundException выбрасывается, если пользователь не найден в таблице user.
     * @throws IncorrectPasswordException выбрасывается, если текущий пароль в NewPassword не совпадает с паролем в таблице user.
     */
    @Override
    public void setPassword(NewPassword newPassword, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException(username);
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Метод для получения информации об аутентифицированном пользователе.
     *
     * @param principal интерфейс для получения username пользователя.
     * @return Dto UserDto.
     * @throws UserNotFoundException выбрасывается, если пользователь не найден в таблице user.
     */
    @Override
    public UserDTO getUser(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return userMapper.userToUserDto(user);
    }

    /**
     * Метод для изменения информации аутентифицированного пользователя.
     *
     * @param updateUserDTO Dto UpdateUserDto.
     * @param principal интерфейс для получения username пользователя.
     * @return Dto UpdateUserDto.
     * @throws UserNotFoundException выбрасывается, если пользователь не найден в таблице user.
     */
    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        user.setFirstname(updateUserDTO.getFirstName());
        user.setLastname(updateUserDTO.getLastName());
        user.setPhone(updateUserDTO.getPhone());
        userRepository.save(user);
        return updateUserDTO;
    }

    /**
     * Метод для изменения аватарки пользователя.
     *
     * @param image картинка с аватаркой.
     * @param principal интерфейс для получения username пользователя.
     * @throws UserNotFoundException выбрасывается, если пользователь не найден в таблице user.
     * @throws IOException выбрасывается, если возникают проблемы при получении картинки.
     */
    @Override
    public void updateUserImage(MultipartFile image, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return;
    }
}
