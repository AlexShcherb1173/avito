package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Сервис для управления пользователями.
 * Предоставляет методы для работы с профилем пользователя, включая получение информации,
 * обновление данных, смену пароля и управление аватаром.
 */
public interface UserService {
    /**
     * Получает информацию о текущем аутентифицированном пользователе.
     *
     * @param username email пользователя
     * @return объект {@link User} с данными пользователя
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     */
    User getCurrentUser(String username);
    /**
     * Обновляет информацию о пользователе.
     * Обновляются только те поля, которые предоставлены в объекте {@link UpdateUser}.
     *
     * @param username email пользователя
     * @param updateUser объект с обновляемыми данными
     * @return обновленный объект {@link User}
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     */
    User updateUser(String username, UpdateUser updateUser);
    /**
     * Обновляет аватар пользователя.
     * Старое изображение удаляется при наличии нового.
     *
     * @param username email пользователя
     * @param image файл изображения для загрузки
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     * @throws RuntimeException если произошла ошибка при сохранении изображения
     */
    void updateUserImage(String username, MultipartFile image);
    /**
     * Изменяет пароль пользователя.
     * Выполняет проверку текущего пароля перед установкой нового.
     *
     * @param username email пользователя
     * @param currentPassword текущий пароль для проверки
     * @param newPassword новый пароль
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     * @throws IllegalArgumentException если текущий пароль неверен
     */
    void updatePassword(String username, String currentPassword, String newPassword);
    /**
     * Получает сущность пользователя по email.
     * Используется для внутренних операций, требующих доступ к сущности.
     *
     * @param username email пользователя
     * @return сущность {@link UserEntity}
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException если пользователь не найден
     */
    UserEntity getUserEntity(String username);
}