package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.util.UploadImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@ExtendWith(MockitoExtension.class)
// @MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityServiceImpl securityService;

    @InjectMocks
    private UserServiceImpl userService;


    private User user;
    private UserDto userDto;
    private User newUser;
    private NewPasswordDto newPasswordDto;
    private UserDto newUserDto2;
    private UserDto newUserDto1;
    private UpdateUserDto updateUserDto;


    @BeforeEach
    void setUp() {
        user = ConstantGeneratorFotTest.userGenerator();
        // Первоначальный пользователь
        userDto = ConstantGeneratorFotTest.userDtoGenerator();
        // Модель для создания первоначального пользователя
        newUser = ConstantGeneratorFotTest.newUserGenerator_3();
        // Пользователь, у которого обновлено поле "image"
        newUserDto2 = ConstantGeneratorFotTest.newUserDtoGenerator_2();
        // Модель с обновленным полем "image"; все остальные поля прежние
        newUserDto1 = ConstantGeneratorFotTest.newUserDtoGenerator_1();
        // Модель, содержащая три обновленных поля: имя, фамилия и телефон; все остальные поля неизменные.
        updateUserDto = ConstantGeneratorFotTest.updateUserDtoGenerator();
        // Модель, содержащая только три обновленных поля: имя, фамилия и телефон
        newPasswordDto = ConstantGeneratorFotTest.newPasswordDtoGenerator();
        // Модель, содержащая только первоначальный и обновленный пароли
    }


    @Test
    void testUpdatePassword_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(true);

        // Пароль, извлеченный из user, и текущий пароль, извлеченный из newPasswordDto, совпадают.
        // Пароль изменен
        boolean isUpdatePassword = userService.updatePassword(newPasswordDto);

        assertTrue(isUpdatePassword);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        // Или
//        verify(passwordEncoder, times(1))
//                .matches("SecretPassword", "SecretPassword");
    }

    @Test
    void testUpdatePassword_IncorrectPassword_ThrowsUnauthorizedException() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        newPasswordDto.setCurrentPassword(USER_INCORRECT_PASSWORD);

        // Пароль, извлеченный из user, и текущий пароль, извлеченный из newPasswordDto, не совпадают,
        // так как пароль был изменен.
        assertThrows(UnauthorizedException.class, () -> userService.updatePassword(newPasswordDto));
        // Исключение выбрасывает тестируемый класс
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testUpdatePassword_UnauthorizedException() {

        // Аутентифицированный пользователь пытается изменить пароль у другого пользователя
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenThrow(new NotFoundException("Пользователь не найден"));
        // Данное исключение выбрасывает зависимый класс.

        assertThrows(ForbiddenException.class, () -> userService.updatePassword(newPasswordDto));
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    // Доступ к этому методу и всем последующим только для аутентифицированных пользователей осуществляется на уровне
    // контроллера. Поэтому в сервисе негативный сценарий выполнения этих методов (закрытие методов для
    // неаутентифицированных пользователей) можно не проверять.

    @Test
    void testGetAuthenticatedUser_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto actual = userService.getAuthenticatedUser();

        assertEquals(userDto, actual);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userMapper, times(1)).toDto(any(User.class));
    }


    @Test
    void testUpdateAuthenticatedUserInfo_Success() {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        doNothing().when(userMapper).updateUserFromUpdateUserDto(updateUserDto, user);
        when(userMapper.toDto(any(User.class))).thenReturn(newUserDto1);

        UserDto expected = UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(NEW_USER_FIRST_NAME)
                .lastName(NEW_USER_LAST_NAME)
                .phone(NEW_USER_PHONE)
                .image(USER_IMAGE)
                .build();

        UserDto actual = userService.updateAuthenticatedUserInfo(updateUserDto);

        assertEquals(expected, actual);
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
    

    @Test
    void testUpdateAuthenticatedUserImage_Success() throws IOException {

        // Необходимо добавить в папку images файл с именем - USER_IMAGE
        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String name = NEW_USER_IMAGE.substring(0, NEW_USER_IMAGE.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, NEW_USER_IMAGE, contentType, content);

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
//        user.setImage(NEW_USER_IMAGE);
//        userDto.setImage(NEW_USER_IMAGE);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        // Ожидаем сохранения в базу данных пользователя с обновленным полем "image"
        when(userMapper.toDto(any(User.class))).thenReturn(newUserDto2);
        // Ожидаем на выходе получить модель пользователя с обновленным полем "image"

        UserDto expected = UserDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .role(USER_ROLE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .phone(USER_PHONE)
                .image(NEW_USER_IMAGE)
                .build();

        // Мокируем вызов статического метода по загрузке картинки
        try (MockedStatic<UploadImage> utilities =  Mockito.mockStatic(UploadImage.class)) {
            // Задаем поведение мокированного метода
            utilities.when(() -> UploadImage.uploadImage(any(MultipartFile.class))).thenReturn("/" + NEW_USER_IMAGE);

            UserDto actual = userService.updateAuthenticatedUserImage(image);

            assertNotNull(actual);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getImage(), actual.getImage());
            verify(securityService, times(1)).getAuthenticatedUserName();
            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(userRepository, times(1)).save(any(User.class));
        }
    }


    @Test
    void testUpdateAuthenticatedUserImage_UnauthorizedException() throws IOException {

        // Доступ к этому методу только для аутентифицированных пользователей осуществляется на уровне контроллера.
        // Экспериментальный тест

        // Необходимо добавить в папку images файл с именем - USER_IMAGE
        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String name = NEW_USER_IMAGE.substring(0, NEW_USER_IMAGE.indexOf("."));
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile(name, NEW_USER_IMAGE, contentType, content);

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new UnauthorizedException("Пользователь не аутентифицирован"));

        assertThrows(UnauthorizedException.class, () -> userService.updateAuthenticatedUserImage(image));
        verify(securityService, times(1)).getAuthenticatedUserName();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testLoadByUserName_Success() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        UserDetails expected = new UserSecurityDetails(user);
        UserDetails actual = userService.loadByUserName(user.getEmail());

        //assertEquals(expected, actual);
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getAuthorities(), actual.getAuthorities());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmailFromDb_Success() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));

        User actual = userService.getUserByEmailFromDb(user.getEmail());

        assertEquals(user, actual);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmailFromDb_ThrowsNotFoundException() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Исключение выбрасывается в тестируемом классе

        assertThrows(NotFoundException.class, () -> userService.getUserByEmailFromDb(user.getEmail()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

}
