package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 *
 * Содержит бизнес-логику получения пользователя,
 * регистрации, обновления профиля и обновления изображения пользователя.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    /**
     * Обновить данные пользователя.
     *
     * @param id идентификатор пользователя
     * @param updateUser DTO с новыми данными пользователя
     * @return Optional с обновленным пользователем
     */
    public Optional<User> updateUser(Integer id, UpdateUser updateUser) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserEntity entity = userOptional.get();
        userMapper.updateUserFields(updateUser, entity);

        UserEntity updatedUser = userRepository.save(entity);
        return Optional.of(userMapper.toDto(updatedUser));
    }

    /**
     * Найти пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с объектом UserEntity
     */
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Преобразовать сущность пользователя в DTO.
     *
     * @param entity сущность пользователя
     * @return DTO пользователя
     */
    public User toDto(ru.skypro.homework.entity.UserEntity entity) {
        return userMapper.toDto(entity);
    }

    /**
     * Обновить изображение пользователя.
     *
     * @param id идентификатор пользователя
     * @param imagePath путь к изображению
     * @return Optional с обновленным пользователем
     */
    public Optional<User> updateUserImage(Integer id, String imagePath) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserEntity entity = userOptional.get();
        entity.setImage(imagePath);

        UserEntity savedUser = userRepository.save(entity);
        return Optional.of(userMapper.toDto(savedUser));
    }

}