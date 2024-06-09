package ru.skypro.homework.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.IncorrectCurrentPasswordException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService implements ru.skypro.homework.service.UserServiceApi {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public User findByUsername(String username) {
        log.info("Поиск пользователя по имени пользователя: {}", username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.error("Пользователь с именем пользователя {} не найден", username);
                    return new UsernameNotFoundException(
                            "Нет пользователя с таким именем пользователя \"" + username + "\""
                    );
                }
        );
    }

    @Override
    public User save(User user) {
        log.info("Сохранение пользователя: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserAvatar(User user, Image image) {
        log.info("Обновление аватара для пользователя: {}", user.getUsername());
        if (user.getAvatar() != null) {
            image.setId(user.getAvatar().getId());
        }
        user.setAvatar(image);
        userRepository.save(user);
    }

    @Override
    public User updateUserDetails(User user, UpdateUser updateUser) {
        log.info("Обновление данных для пользователя: {}", user.getUsername());
        user.setPhone(updateUser.getPhone());
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        return userRepository.save(user);
    }

    @Override
    public void changeUserPassword(User user, NewPassword password) {
        log.info("Изменение пароля для пользователя: {}", user.getUsername());
        if (!encoder.matches(password.getCurrentPassword(), user.getPassword())) {
            log.error("Неверный текущий пароль для пользователя: {}", user.getUsername());
            throw new IncorrectCurrentPasswordException("Текущий пароль неверен");
        }
        user.setPassword(encoder.encode(password.getNewPassword()));
        userRepository.save(user);
    }

}
