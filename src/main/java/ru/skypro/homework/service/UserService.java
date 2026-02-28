package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperManual;
import ru.skypro.homework.repository.UserRepository;
/**
 * Сервис для работы с пользователями.
 * <p>
 * Возвращает профиль текущего пользователя, обновляет данные профиля,
 * меняет пароль и обновляет изображение пользователя.
 * Все операции выполняются для пользователя, определяемого по email из аутентификации.
 * </p>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    //private final UserMapper userMapper;
    private final UserMapperManual userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Возвращает пользователя по email.
     *
     * @param email email пользователя (username в Basic Auth)
     * @return DTO {@link User}
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    /**
     * Обновляет данные профиля пользователя по email.
     * Обновляются только ненулевые поля DTO (логика зависит от MapStruct-конфигурации).
     *
     * @param email email пользователя
     * @param updateUser DTO с обновляемыми полями
     * @return DTO {@link User} после обновления
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден
     */
    public User updateUserByEmail(String email, UpdateUser updateUser) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));


        userMapper.applyUpdate(updateUser, entity);

        return userMapper.toDto(entity);
    }

    /**
     * Меняет пароль пользователя.
     *
     * @param email email пользователя
     * @param dto DTO с текущим и новым паролем
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден или текущий пароль неверный
     */
    public void setPassword(String email, NewPassword dto) {
        if (dto == null || dto.getCurrentPassword() == null || dto.getNewPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password fields are required");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    /**
     * Обновляет изображение пользователя.
     * <p>В текущей реализации сохраняется только строка imagePath.</p>
     *
     * @param email email пользователя
     * @param imagePath путь/имя файла изображения
     * @return массив байт изображения (в текущей реализации возвращается пустой массив)
     * @throws org.springframework.web.server.ResponseStatusException если пользователь не найден
     */
    public byte[] updateUserImage(String email, String imagePath) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        user.setImage(imagePath);

        return new byte[0];
    }
}

