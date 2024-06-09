package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

public interface AuthServiceApi {

    /**
     * Авторизация пользователя
     *
     * @param userName Имя пользоватля
     * @param password Пароль пользоватля
     * @return true - пользователь авторизован, false - в противном случае
     */
    boolean login(String userName, String password);

    /**
     * Регистрация нового пользователя
     *
     * @param register данные для регистрации нового пользователя {@link Register}
     * @return true - пользователь зарегистрирован, false - в противном случае
     */
    boolean register(Register register);
}
