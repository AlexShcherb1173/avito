package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

/**
 * Сервисный интерфейс для работы с пользователями.
 *
 * Интерфейс предоставляет методы для выполнения операций с пользователями:
 * - Преобразование сущности {@link User} в {@link UserDTO} {@link #getUserDTO(User)}.
 * - Обновление данных пользователя {@link #updateUser(UpdateUserDTO)}.
 * - Получение текущего пользователя в виде DTO {@link #getCurrentUser()}.
 * - Обновление изображения пользователя {@link #updateUserImage(MultipartFile)}.
 * - Создание сущности {@link User} на основе данных {@link UserDTO} {@link #createUserFromDTO(UserDTO)}.
 * - Получение текущего пользователя как сущности {@link #getCurrentUserEntity()}.
 * - Обновление пароля пользователя {@link #updatePassword(NewPasswordDTO)}.
 *
 * Методы интерфейса возвращают объекты DTO, такие, как {@link UserDTO}, а также выполняют операции обновления и получения данных.
 */
public interface UserService {

    UserDTO getUserDTO(User user);

    UserDTO updateUser(UpdateUserDTO updateUserDTO);

    UserDTO getCurrentUser();

    UserDTO updateUserImage(MultipartFile image);

    User createUserFromDTO(UserDTO userDTO);

    User getCurrentUserEntity();

    void updatePassword(NewPasswordDTO passwordDTO);
}
