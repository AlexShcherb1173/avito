package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Класс по регистрации и аутентификации пользователя
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    /**
     * Метод по регистрации пользователя.
     * Осуществляем в базе данных поиск пользователя по введенному им логину.
     * Если в базе данных уже зарегистрирован пользователь с таким логином,
     * то будет выброшено соответствующее исключение.
     * Передаем в параметры метода encode() пароль, извлеченный из модели для регистрации пользователя,
     * и уже закодированный пароль сохраняем в модель для регистрации пользователя.
     * Через маппер сохраняем логин и пароль пользователя в объект User, а User сохраняем в базу данных.
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getUsername())) {
//        if (userService.getUserByEmailFromDb(registerDto.getUsername()) != null) {
            log.info("Пользователь с таким логином = {} уже зарегистрирован", registerDto.getUsername());
            throw new UserAlreadyRegisteredException("Пользователь с таким логином уже зарегистрирован");
        }
        registerDto.setPassword(encoder.encode(registerDto.getPassword()));
        User user = userMapper.toEntityFromRegisterDto(registerDto);
        userRepository.save(user);
        log.info("Регистрация прошла успешно! Логин пользователя {}", registerDto.getUsername());
        return true;
    }


    /**
     * Метод по аутентификации пользователя.
     * Получаем из базы данных пользователя по введенному пользователем логину.
     * Если введенный пользователем логин неправильный, то будет выброшено соответствующее исключение.
     * Если в базе данных по введенному логину найден пользователь, то получаем объект UserDetails, интегрирующий
     * Spring Security c конкретной моделью пользовательских данных и требованиями аутентификации нашего приложения.
     * Если введенный пользователем пароль не совпадет с паролем, извлеченным из объекта UserDetails, то будет выброшено
     * исключение о неправильно введенном пароле.
     */
    @Override
    public boolean login(LoginDto loginDto) {
        UserSecurityDetails userSecurityDetails;
        try {
            // В этом случае объект Authentication еще не создан. Поэтому загружаем пользователя из базы данных
            // по введенному им логину
            userSecurityDetails = (UserSecurityDetails) manager.loadUserByUsername(loginDto.getUsername());
        } catch (NotFoundException e) {
            log.error("Введен неправильный логин = {}", loginDto.getUsername());
            throw new UnauthorizedException("Введен неправильный логин или пароль", e);
            // Существует исключение UsernameNotFoundException extends AuthenticationException. Но в данном случае
            // выбрасывается пользовательское исключение NotFoundException. Оборачиваем это исключение в пользовательское
            // исключение UnauthorizedException
        }

        boolean isPasswordsMatch = encoder.matches(loginDto.getPassword(), userSecurityDetails.getPassword());
        // Используется метод matches() класса PasswordEncoder для сравнения предоставленного пароля в модели loginDto
        // с закодированным паролем, хранящимся в UserDetails.
        // Метод matches() автоматически извлекает соль из хешированного пароля, использует ее для хеширования исходного
        // пароля и сравнивает полученные хеши.
        if (!isPasswordsMatch) {
            log.error("Введен неправильный пароль = {}", loginDto.getPassword());
            throw new UnauthorizedException("Введен неправильный логин или пароль");
        }
        log.info("Пользователь прошел аутентификацию");
        return true;
    }

}

