package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.register.NewPassword;
import ru.skypro.homework.dto.register.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.SecurityService;
import ru.skypro.homework.service.UserService;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для управления профилем пользователя, включая
 * обновление пароля, получение информации о пользователе, обновление
 * профиля и аватара.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;
    private final ImageService imageService;

    /**
     * Конструктор сервиса пользователей.
     *
     * @param userRepository  - репозиторий для работы с пользователями
     * @param userMapper      - маппер для преобразования между DTO и сущностями
     * @param passwordEncoder - кодировщик паролей для безопасного хранения
     * @param securityService - сервис для работы с безопасностью
     * @param imageService    - сервис для работы с изображениями
     */
    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           SecurityService securityService,
                           ImageService imageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        this.imageService = imageService;
    }

    /**
     * Обновляет пароль текущего пользователя.
     * Метод проверяет текущий пароль пользователя и, если он верен,
     * обновляет его на новый с применением шифрования.
     *
     * @param newPassword - объект с текущим и новым паролем
     * @throws UserNotFoundException    если текущий пользователь не найден в базе данных
     * @throws InvalidPasswordException если текущий пароль указан неверно
     * @see NewPassword
     */
    @Override
    public void updatePassword(NewPassword newPassword) {
        UserEntity userEntity = securityService.getCurrentUser();

        // Проверяем текущий пароль
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            throw new InvalidPasswordException("Неверный текущий пароль");
        }

        // Обновляем пароль
        userEntity.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(userEntity);
    }

    /**
     * Получает информацию о текущем пользователе.
     * Метод извлекает данные текущего аутентифицированного пользователя
     * и преобразует их в DTO для передачи клиенту.
     *
     * @return объект User с информацией о текущем пользователе
     * @throws UserNotFoundException если текущий пользователь не найден в базе данных
     * @see User
     */
    @Override
    public User getInfoAboutUser() {
        UserEntity userEntity = securityService.getCurrentUser();
        return userMapper.toUserDto(userEntity);
    }

    /**
     * Обновляет информацию о текущем пользователе.
     * Метод обновляет основные данные пользователя: имя, фамилию и телефон.
     * Обновление выполняется только для полей, которые не null в переданном объекте.
     *
     * @param newInfoUser - объект с новыми данными пользователя
     * @return объект UpdateUser с обновленными данными
     * @throws UserNotFoundException если текущий пользователь не найден в базе данных
     * @see UpdateUser
     */
    @Override
    public UpdateUser updateInfoAboutUser(UpdateUser newInfoUser) {
        UserEntity userEntity = securityService.getCurrentUser();

        // Обновляем данные пользователя
        userMapper.updateUserEntityFromDto(userEntity, newInfoUser);
        userRepository.save(userEntity);

        return newInfoUser;
    }

    /**
     * Обновляет аватар текущего пользователя.
     * Метод сохраняет новое изображение аватара для текущего пользователя.
     *
     * @param image - файл изображения аватара
     * @throws UserNotFoundException если текущий пользователь не найден в базе данных
     */
    @Override
    public void updateAvatarUser(MultipartFile image) {
        UserEntity userEntity = securityService.getCurrentUser();

        // Сохраняем изображение
        String imagePath = imageService.saveImage(image);

        // Обновляем путь к аватару
        userEntity.setImagePath(imagePath);
        userRepository.save(userEntity);
    }

    /**
     * Получает аватар текущего пользователя.
     *
     * @return изображение аватара
     * @throws UserNotFoundException если текущий пользователь не найден в базе данных
     */
    @Override
    public byte[] getUserAvatar() {
        return imageService.getUserAvatar();
    }
}
