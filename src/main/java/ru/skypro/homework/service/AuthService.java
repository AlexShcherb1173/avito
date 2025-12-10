package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Обеспечивает базовые функции безопасности приложения.
 */
public interface AuthService {

    /**
     * Выполняет аутентификацию пользователя.
     * Проверяет соответствие предоставленных учетных данных.
     *
     * @param userName email пользователя
     * @param password пароль пользователя
     * @return true если аутентификация успешна, false если учетные данные неверны
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя в системе.
     * Выполняет проверку на уникальность email.
     * При отсутствии указанной роли устанавливается роль USER по умолчанию.
     *
     * @param register данные для регистрации
     * @return true если регистрация успешна, false если пользователь с таким email уже существует
     * @throws org.springframework.dao.DataIntegrityViolationException при нарушении ограничений базы данных
     */
    boolean register(Register register);
}