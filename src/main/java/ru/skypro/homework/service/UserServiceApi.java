package ru.skypro.homework.service;

import jakarta.transaction.Transactional;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

public interface UserServiceApi {

    /**
     * Поиск пользователя по логину
     *
     * @param username логин пользователя
     * @return {@link User}
     */
    User findByUsername(String username);

    /**
     * Сохранить нового пользователя
     *
     * @param user пользователь которого надо сохранить {@link User}
     * @return {@link User} сохраненный пользователь
     */
    User save(User user);

    /**
     * Обновить аватар пользователя
     *
     * @param user  пользователь у которого надо изменить аватар {@link User}
     * @param image новый аватар {@link Image}
     */
    @Transactional
    void updateUserAvatar(User user, Image image);

    /**
     * Обновить данные пользователя
     *
     * @param user       пользователь которого надо сохранить {@link User}
     * @param updateUser данные которые нужно изменить {@link UpdateUser}
     * @return {@link User} c измененными данными
     */
    User updateUserDetails(User user, UpdateUser updateUser);

    /**
     * Обновить пароль пользователя
     *
     * @param user     пользователь которому нужно обновить пароль
     * @param password {@link NewPassword} новый и старый пароли
     */
    void changeUserPassword(User user, NewPassword password);
}
