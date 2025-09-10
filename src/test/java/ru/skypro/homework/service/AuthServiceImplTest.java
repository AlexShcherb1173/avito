package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.USER_INCORRECT_PASSWORD;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;


    private User user;
    private User newUser;
    private RegisterDto registerDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {

        user = ConstantGeneratorFotTest.userGenerator();
        // Зарегистрированный пользователь
        newUser = ConstantGeneratorFotTest.newUserGenerator_1();
        // Новый зарегистрированный пользователь
        registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        // Модель для регистрации пользователя
        loginDto = ConstantGeneratorFotTest.loginDtoGenerator();
        // Модель для аутентификации и авторизации пользователя
    }

    @Test
    void testRegister_Success() {

        // Пользователь с таким логином отсутствует в базе данных
        when(userRepository.existsByEmail(registerDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodeNewPassword");
        when(userMapper.toEntityFromRegisterDto(any(RegisterDto.class))).thenReturn(newUser);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        boolean isRegister = authService.register(registerDto);

        assertTrue(isRegister);
        verify(userRepository, times(1)).existsByEmail(registerDto.getUsername());
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testRegister_ThrowsUserAlreadyRegisteredException() {

        // Пользователь с таким логином уже существует в базе данных
        when(userRepository.existsByEmail(registerDto.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyRegisteredException.class, () -> authService.register(registerDto));
        // Исключение возвращает тестируемый, а не зависимый класс. Поэтому не мокируем поведение на выброс исключения.
        verify(userRepository, times(1)).existsByEmail(registerDto.getUsername());
    }


    @Test
    void testLogin_Success() {

        UserSecurityDetails userSecurityDetails = new UserSecurityDetails(user);
        // При аутентификации пользователя через логин и пароль необходимо найти по логину пользователя в базе данных
        // и создать объект UserSecurityDetails для интеграции пользователя со SpringSecurity

        when(userDetailsService.loadUserByUsername(loginDto.getUsername())).thenReturn(userSecurityDetails);
        // При выгрузке из базы данных пользователя по логину ожидаем получить объект UserSecurityDetails
        when(passwordEncoder.matches(loginDto.getPassword(), userSecurityDetails.getPassword())).thenReturn(true);
        // Когда извлекаем из модели пароль, введенный пользователем, и сравниваем его с паролем, извлеченным из объекта
        // UserSecurityDetails, то ожидаем получить true

        boolean isLogin = authService.login(loginDto);

        assertTrue(isLogin);
        verify(userDetailsService, times(1)).loadUserByUsername(loginDto.getUsername());
        verify(passwordEncoder, times(1))
                .matches("SecretPassword", "SecretPassword");
    }


    @Test
    void testLogin_IncorrectLogin_ThrowsUnauthorizedException() {

        // При аутентификации пользователя через логин и пароль необходимо найти по логину пользователя в базе данных
        // и создать объект UserSecurityDetails для интеграции пользователя со SpringSecurity

        when(userDetailsService.loadUserByUsername(loginDto.getUsername()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        // Зависимый класс UserDetailsService, в случае не обнаружения пользователя в базе данных, выбрасывает
        // пользовательское исключение NotFoundException. Поэтому мокируем поведение этого метода на выброс исключения.

        assertThrows(UnauthorizedException.class, () -> authService.login(loginDto));
        // В тестируемом классе исключение NotFoundException перехвачено и обернуто в пользовательское исключение
        // UnauthorizedException
        verify(userDetailsService, times(1)).loadUserByUsername(loginDto.getUsername());
    }

    @Test
    void testLogin_IncorrectPassword_ThrowsUnauthorizedException() {

        UserSecurityDetails userSecurityDetails = new UserSecurityDetails(user);

        when(userDetailsService.loadUserByUsername(loginDto.getUsername())).thenReturn(userSecurityDetails);
        // При выгрузке из базы данных пользователя по логину ожидаем получить объект UserSecurityDetails

        loginDto.setPassword(USER_INCORRECT_PASSWORD);
        when(passwordEncoder.matches(loginDto.getPassword(), userSecurityDetails.getPassword())).thenReturn(false);
        // Когда извлекаем из модели пароль, введенный пользователем, и сравниваем его с паролем, извлеченным из объекта
        // UserSecurityDetails, то ожидаем получить false

        assertThrows(UnauthorizedException.class, () -> authService.login(loginDto));

        verify(userDetailsService, times(1)).loadUserByUsername(loginDto.getUsername());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

}
