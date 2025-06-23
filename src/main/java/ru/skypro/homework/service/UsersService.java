package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

import java.util.List;

public interface UsersService {

    /**
     * Устанавливает новый пароль для пользователя.
     *
     * @param newPassword DTO с именем пользователя и новым паролем
     */
    void setPassword(NewPassword newPassword);

    /**
     * Возвращает DTO текущего аутентифицированного пользователя.
     *
     * @return UserDTO с информацией о пользователе
     */
    UserDto getCurrentUser();

    /**
     * Обновляет данные текущего пользователя.
     *
     * @param updateUser DTO с обновлёнными данными пользователя
     * @return обновлённый UserDTO
     */
    UserDto updateUser(UpdateUserDto updateUser);

    /**
     * Обновляет аватар текущего пользователя.
     *
     * @param userId ID пользователя, которому принадлежит аватар
     * @param file   файл с изображением аватара
     */
    void updateImage(Integer userId, MultipartFile file);


    void updateImage(MultipartFile file);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список UserDTO с информацией о пользователях
     */
    List<UserDto> getAllUsers();

    User createUser(UserDto userDto);
}
