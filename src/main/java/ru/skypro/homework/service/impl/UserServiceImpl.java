package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.IncorrectPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * <b> Сервис для работы с пользователями </b> <p>
 * Содержит CRUD методы
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final AvatarService avatarService;
    private final PasswordEncoder encoder;

    /**
     * <b>Метод изменения пароля </b> <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} , далее с помощью {@link #encoder} проверяет совпадение действующего пароля и пароля
     * аутентифицированного пользователя, если пароли равны, хэширует пароль и сохраняет его в объект пользователя,
     * далее пользователь сохраняется в БД
     * @param newPassword {@link NewPassword})
     * @param authentication {@link Authentication})
     * @throws IncorrectPasswordException не корректный действующий пароль
     */
    @Override
    public void setPassword(NewPassword newPassword, Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        if (encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(newPassword.getNewPassword()));
            repository.save(user);
            return;
        }
        throw new IncorrectPasswordException("Неверный пароль");

    }

    /**
     * <b>Метод получения пользователя </b> <p>
     * @param authentication {@link Authentication}
     * @return {@link UserDTO}
     */
    @Override
    public UserDTO getUser(Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        return mapper.userToUserDto(user);
    }

    /**
     *<b>Метод изменения информации о пользователе </b> <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} , меняет в пользователе: имя, фамилию, номер телефона.
     * Сохраняет пользователя.
     * @param update {@link UpdateUser} (DTO)
     * @param authentication  {@link Authentication}
     * @return {@link UpdateUser}
     */
    @Override
    public UpdateUser updateUserInfo(UpdateUser update, Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        user.setFirstName(update.getFirstName());
        user.setLastName(update.getLastName());
        user.setPhone(update.getPhone());
        repository.save(user);
        return update;
    }

    /**
     * <b>Метод изменения аватарки пользователя </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} ,отдельно сохраняет действующий аватар, далее дергает метод загрузки аватарки у
     *  {@link #avatarService}, и передает файл из параметров. В конце удаляет "старую" аватарку и пересохраняет пользователя
     *
     * @param image {@link MultipartFile} аватарка (файл)
     * @param authentication  {@link Authentication}
     * @throws IOException (может выкинуть ошибки загрузки)
     */
    @Override
    @Transactional
    public void updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        Avatar imageFile = user.getAvatar();
        user.setAvatar(avatarService.uploadAvatar(image));
        if (imageFile != null) {
            avatarService.removeAvatar(imageFile);
        }
        repository.save(user);
    }
}
