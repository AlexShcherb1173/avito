package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exceptions.PasswordsNotEqualsException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.map.UserMap;

import java.io.IOException;
import java.security.Principal;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private UserMap userMap;
    private final PasswordEncoder encoder;

    /**
     * Изменить пароль
     *
     * @param newPasswordDto = старый пароль + новый пароль
     */
    @Override
    public void setPassword(NewPasswordDto newPasswordDto, Principal principal) {
        User user = findByUsername(principal);
        if (!encoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new PasswordsNotEqualsException();
        }
        user.setPassword(encoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(user);

    }

    /**
     * Получение информации об авторизованном пользователе
     *
     * @param principal = интерфейс для получения username пользователя
     * @return = UserDto
     */
    @Override
    public UserDto getInfoAboutAuthUser(Principal principal) {
        User user = findByUsername(principal);
        return userMap.mapUserDto(user);

    }

    /**
     * Обновление информации об авторизованном пользователе
     *
     * @param principal = интерфейс для получения username пользователя
     * @param updateUserDto = данные для изменения
     * @return = измененного User
     */
    @Override
    public UpdateUserDto setInfoAboutAuthUser(Principal principal, UpdateUserDto updateUserDto) {
        User user = findByUsername(principal);
        User resul = userMap.updateUser(user, updateUserDto);
        return userMap.mapUpdateUserDto(userRepository.save(resul));
    }

    /**
     * Обновление аватара авторизованного пользователя
     *
     * @param principal = интерфейс для получения username пользователя
     * @param image     = путь к файлу картинки
     * @throws IOException выбрасывается, если возникают проблемы при получении картинки.
     */
    @Override
    public void setAvatar(MultipartFile image, Principal principal) {
        User user = findByUsername(principal);
        try {
            user.setImage(image.getBytes());
        } catch (IOException e) {
            log.info("Ошибка с записью изображения");
        }
        userRepository.save(user);
    }

    /**
     * Метод для получения User по имени
     * @param principal = получаем Principal
     * @return = User
     * @throws UserNotFoundException выбрасывается, если пользователь не найден в таблице user.
     */
    public User findByUsername(Principal principal) {
        String userName = principal.getName();
        return userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(userName));
    }

    /**
     * Метод для получения User из БД по id
     * @param id = id User
     * @return = User
     */
    public User findByUserId(int id) {
        return userRepository.findById(id).orElseThrow();
    }
}
