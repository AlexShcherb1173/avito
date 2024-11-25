package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetailsService;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserAlreadyAddException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import javax.validation.constraints.NotNull;

/**
 * Сервис аутентификации пользователя (проверка подлинности пользователя путём сравнения введённого им пароля с паролем,
 * сохранённым в базе данных пользовательских логинов)
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsService userDetailsService;
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;


    /**
     * <b> Метод для аутентификации уже зарегистрированного пользователя (вход) </b> <p>
     * Принцип работы:<p>
     * Получить на вход логин и пароль, найти пользователя по логину с помощью метода loadUserByUsername из класса {@link MyUserDetailsService},
     * вернуть его как userDetails и у этого userDetails сравнить текущий и переданный пароли с помощью метода matches,
     * который находится в {@link PasswordEncoder}
     *
     * @param userName логин
     * @param password пароль
     * @return true - при удачной аутентификации, {@link BadCredentialsException} при неудачной
     */
    @Override
    public boolean login(@NotNull String userName, @NotNull String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if (encoder.matches(password, userDetails.getPassword())) {
            return true;
        }
        throw new BadCredentialsException("Неверный логин или пароль");
    }

    /**
     * <b> Метод для регистрации нового пользователя </b> <p>
     * Принцип работы:<p>
     * Преобразовать полученное DTO в User, проверить если такой пользователь в БД, если есть - выкинуть исключение,
     * если нет, то захешировать пароль с помощью  {@link PasswordEncoder}, а после сохранить этого пользователя в БД.
     *
     * @param register DTO пользователя хранящая поля регистрации
     * @return true - при удачной регистрации,  {@link UserAlreadyAddException} при неудачной
     */
    @Override
    public boolean register(Register register) {
        User user = mapper.registerToUser(register);
        if (repository.existsUserByEmailIgnoreCase(user.getEmail())) {
            throw new UserAlreadyAddException("Пользователь уже добавлен!");
        }
        user.setPassword(encoder.encode(register.getPassword()));
        repository.save(user);
        return true;

    }
}