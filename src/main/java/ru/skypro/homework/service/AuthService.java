package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Интерфейс для сервисов аутентификации пользователей.
 * Предоставляет методы для входа в систему и регистрации новых пользователей.
 */
public interface AuthService {
    /**
     * <b>Выполняет вход пользователя в систему. </b> <p>
     *
     * @param userName имя пользователя, используемое для входа
     * @param password пароль пользователя, используемый для входа
     * @return true, если вход выполнен успешно; false в противном случае
     */
    boolean login(String userName, String password);

    /**
     * <b>Регистрирует нового пользователя в системе. </b> <p>
     *
     * @param register объект, содержащий информацию о новом пользователе,
     *                 такую как имя, пароль и другие необходимые данные
     * @return true, если регистрация прошла успешно; false в противном случае
     */
    boolean register(Register register);
}
