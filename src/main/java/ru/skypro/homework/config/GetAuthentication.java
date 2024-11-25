package ru.skypro.homework.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserNotFoundException;

/**
 * Конфиг - класс для получения аутентифицированного пользователя,
 * класс содержащий единственный метод {@link #getAuthenticationUser}
 */
@Component
public class GetAuthentication {

    /**
     * <b> Метод получения аутентифицированного пользователя </b> <p>
     * (получение пользователя из контекста)
     * Принцип работы:<p>
     * На вход получаем логин пользователя, далее логин достаем из контекста аутентификации,
     * сравниваем два логина, если это один и тот же логин, то пользователь аутентифицирован и
     * мы вынимаем сначала из аутентификации сущность в виде userDetails, а потом из userDetails достаём User и возвращаем его
     * в качестве итогового значения.
     * @param userName логин
     * @return {@link User}
     * @throws UserNotFoundException исключение о не авторизованном пользователе
     */
    public User getAuthenticationUser(String userName) throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equals(userName)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getUser();
        }
        throw new UsernameNotFoundException("Не авторизованный пользователь");
    }
}
