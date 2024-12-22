package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.ApplicationConfig;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyExistsException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CustomUserDetailsManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 */
@Service
public class CustomUserDetailsManagerImpl implements CustomUserDetailsManager {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsManagerImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsManagerImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загружает пользователя по ИД из БД
     *
     * @param username имя пользователя
     * @return UserDetails
     * @throws UsernameNotFoundException ошибка. ну а что не понятно...
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserMapper.toUserDetails(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    /**
     * Проверка логина на уникальность
     *
     * @param username имя пользователя
     * @return boolean найден ли пользователь с таким username
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Новый пользователь из регистрационного эндпоинта
     *
     * @param user UserDetails
     */
    @Override
    public void createUser(UserDetails user) throws UserAlreadyExistsException {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        userRepository.save(UserMapper.toUser(user));
    }
    /**
     * Создание нового пользователя.
     */
    @Override
    public void createUser(RegisterDto user) {
        if (userExists(user.getUsername())) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        User userEncoded = UserMapper.toEncodedUser(user);
        userRepository.save(userEncoded);
    }
    /**
     * Обновление пользователя UserDetails.
     */
    @Override
    public void updateUser(UserDetails user) {
        updateUser(UserMapper.toUser(user));
    }
    /**
     * Обновление пользователя User.
     */
    @Override
    public void updateUser(User user) {
        if (!userExists(user.getUsername())) {
            throw new UsernameNotFoundException(user.getUsername());
        }
        userRepository.save(user);
    }
    /**
     * Обновление пользователя DTO.
     */
    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto) {
        User user = getCurrentUser();
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());
        userRepository.save(user);
        return updateUserDto;
    }
    /**
     * Удаление пользователя.
     */
    @Override
    public void deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        userRepository.delete(user.get());
    }
    /**
     * Изменение пароля DTO.
     */
    @Override
    public void changePassword(NewPasswordDto newPasswordDto) {
        changePassword(newPasswordDto.getCurrentPassword(), newPasswordDto.getNewPassword());
    }
    /**
     * Изменение пароля.
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        if (encoder.matches(oldPassword, getCurrentUser().getPassword())) {
            User user = getCurrentUser();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        }
    }
    /**
     * Получение текущего пользователя.
     */
    @Override
    public User getCurrentUser() throws UnauthorizedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new UnauthorizedException("Пользователь не авторизован");
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));
    }
    /**
     * Изменение аватара пользователя.
     */
    @Override
    public void updateUserAvatar(MultipartFile image) throws IOException {
        User user = getCurrentUser();
        if (user.getAvatar() != null) {
            log.info("Найден аватар, удаляю");
            deleteExistingAvatar(user);
        }
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        user.setAvatar(UUID.randomUUID() + "." + extension);
        Path parentDir = Path.of(".", ApplicationConfig.getPathToAvatars());
        if (!Files.exists(parentDir)) Files.createDirectories(parentDir);
        Path path = parentDir.resolve(user.getAvatar());
        Files.write(path, image.getBytes());
        userRepository.save(user);
    }
    /**
     * Удаление существующего аватара.
     */
    @Override
    public void deleteExistingAvatar(User user) throws IOException {
        Path path = resolvePathToAvatar(user.getAvatar());
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
    /**
     * Получение аватара из БД.
     */
    @Override
    public byte[] getAvatarFromFs(String avatarId) throws IOException {
        Path path = resolvePathToAvatar(avatarId);
        if (!Files.exists(path)) {
            log.info("Аватара нету");
            throw new FileNotFoundException(avatarId);
        }
        return Files.readAllBytes(path);
    }

    /**
     * Разрешение пути к аватару.
     */
    private Path resolvePathToAvatar(String avatarId) {
        return Path.of(".", ApplicationConfig.getPathToAvatars()).resolve(avatarId);
    }
}
