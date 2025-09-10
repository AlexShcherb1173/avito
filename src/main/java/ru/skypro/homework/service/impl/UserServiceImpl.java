package ru.skypro.homework.service.impl;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.UploadImage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SecurityServiceImpl securityService;
    private final PasswordEncoder encoder;

    /**
     * Метод по обновлению пароля.
     * В качестве аргумента данный метод принимает модель по обновлению пароля, содержащую текущий пароль и новый пароль.
     * Из контекста безопасности получаем логин текущего пользователя, по логину получаем самого пользователя.
     * Если пароль, извлеченный из текущего пользователя, совпадает с паролем, введенным данным пользователем,
     * то обновляем пароль пользователя, получив новый пароль из модели по обновлению пароля (тоже введенный пользователем).
     * @param newPassword - новый пароль
     * @return true, если пароль, введенный пользователем, совпал с паролем текущего пользователя.
     * @throws UnauthorizedException, если введен неправильный текущий пароль
     */
    @Override
    @Transactional
    public boolean updatePassword(NewPasswordDto newPassword) {
        String email = securityService.getAuthenticatedUserName();
        // В этом случае пользователь аутентифицирован, и в контексте безопасности уже хранится объект
        // Authentication. Поэтому логин пользователя извлекаем из объекта Authentication
        User user;
        try {
            user = getUserByEmailFromDb(email);
        } catch (NotFoundException e) {
            throw new ForbiddenException("Отсутствуют права доступа к запрошенному ресурсу");
        }

        // В базе данных пароли хранятся в зашифрованном виде. Раскодировать их невозможно. Поэтому текущий пароль,
        // извлеченный из NewPasswordDto, также зашифровываем с помощью этого же самого кодировщика и сравниваем с
        // паролем, извлеченным из пользователя из базы данных
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            log.error("Введен неправильный текущий пароль = {}", newPassword.getCurrentPassword());
            throw new UnauthorizedException("Введен неправильный текущий пароль");
        }

        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getAuthenticatedUser() {
        String email = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        return userMapper.toDto(user);

        // @Transactional(readOnly = true) - Метод будет выполняться в транзакции только для чтения
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto updateAuthenticatedUserInfo(UpdateUserDto updateUser) {
        String email = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(email);
        userMapper.updateUserFromUpdateUserDto(updateUser, user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDto updateAuthenticatedUserImage(MultipartFile file) {
        String username = securityService.getAuthenticatedUserName();
        User user = getUserByEmailFromDb(username);

        String urlImage = UploadImage.uploadImage(file);
        user.setImage(urlImage);
        // В сущность User сохраняется путь к файлу, состоящий из имени файла (без имени папки)
        // В данном случае "/" - не удаляем, поскольку URL - путь, по которому фронтенд будет искать файл с картинкой,
        // будет содержать только "/"

        userRepository.save(user);
        return userMapper.toDto(user);
    }

//    @Override
//    @Transactional(isolation = Isolation.REPEATABLE_READ)
//    public boolean saveUser(User user) {
//        return userRepository.save(user);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public boolean emailExists(String email) {
//        return userRepository.existsByEmail(email);
//    }


    /**
     *
     * @param username - логин пользователя
     * @return объект UserSecurityDetails, необходимый для интеграции Spring Security с конкретной моделью
     * пользовательских данных и требованиями аутентификации конкретного приложения.
     */
    @Override
    public UserDetails loadByUserName(String username) {
        User user = getUserByEmailFromDb(username);
        return new UserSecurityDetails(user);

        // Реализуем метод loadByUserName(String username) интерфейса UserDetailsService, который использует метод
        // getUserByEmailFromDb(String email) для получения данных пользователя из базы и преобразует их в объект
        // UserSecurityDetails
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmailFromDb(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
