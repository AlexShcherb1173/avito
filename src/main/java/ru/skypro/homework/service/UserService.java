package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDTO;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * Интерфейс UserService предоставляет методы для управления пользователями,
 * включая обновление пароля, получение информации о пользователе,
 * обновление информации о пользователе и обновление аватара.
 */
public interface UserService {
    /**
     * <b>Устанавливает новый пароль для пользователя. </b> <p>
     *
     * @param newPassword объект, содержащий новый пароль.
     * @param authentication объект аутентификации, представляющий текущего пользователя.
     */
    void setPassword(NewPassword newPassword, Authentication authentication);

    /**
     * <b>Получает информацию о текущем пользователе. </b> <p>
     *
     * @param authentication объект аутентификации, представляющий текущего пользователя.
     * @return объект UserDTO, содержащий информацию о пользователе.
     */
    UserDTO getUser(Authentication authentication);

    /**
     * <b>Обновляет информацию о пользователе. </b> <p>
     *
     * @param update объект, содержащий обновленные данные пользователя.
     * @param authentication объект аутентификации, представляющий текущего пользователя.
     * @return объект UpdateUser с обновленной информацией.
     */
    UpdateUser updateUserInfo(UpdateUser update, Authentication authentication);

    /**
     * <b>Обновляет аватар пользователя. </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     *
     * @param image файл изображения, представляющий новый аватар.
     * @param authentication объект аутентификации, представляющий текущего пользователя.
     * @throws IOException если произошла ошибка при обработке файла изображения.
     */
    @Transactional
    void updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException;

}
