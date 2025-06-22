package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.FileStorageException;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.ImageDto;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.ImageMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;


import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Override
    public void setPassword(NewPassword newPassword) {
        User user = currentUserService.getCurrentUser();
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser() {
        User user = currentUserService.getCurrentUser();
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUser) {
        User user = currentUserService.getCurrentUser();
        userMapper.updateEntityFromDto(updateUser, user);
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public void updateImage(Integer userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с Id " + userId + " не найден!"));
        checkAndSaveImage(Objects.requireNonNull(file), user);
    }

    @Override
    public void updateImage(MultipartFile file) {
        User user = currentUserService.getCurrentUser();
        checkAndSaveImage(Objects.requireNonNull(file), user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        user.setEmail(userDto.getEmail());

        log.info("Creating user with username: {}, email: {}, role: {}", user.getUsername(), user.getEmail(), user.getRole());
        return userRepository.save(user);
    }
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    public void checkAndSaveImage(MultipartFile file, User user) {
        try {
            // Валидация файла
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Файл не может быть пустым");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("Размер файла превышает допустимый лимит");
            }

            // Безопасное получение имени файла
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFilename);
            String safeFilename = UUID.randomUUID() + "." + fileExtension; // Генерируем уникальное имя

            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(safeFilename);

            // Создаем директории, если их нет
            Files.createDirectories(uploadPath);

            // Проверяем безопасность пути
            if (!filePath.normalize().startsWith(uploadPath)) {
                throw new SecurityException("Попытка сохранить файл вне целевой директории");
            }

            // Сохраняем файл
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            ImageDto image = new ImageDto();
            image.setImageUrl(safeFilename);
            image.setUserId(user.getId());
            try {
                image.setData(file.getBytes());
            } catch (IOException e) {
                log.error("Ошибка чтения файла для пользователя {}: {}", user.getId(), e.getMessage());
                throw new RuntimeException("Ошибка четения файла поробуйте снова.", e);
            }

            // Сохраняем информацию в БД
            imageService.saveToDatabase(image, filePath, file);

            log.info("Аватар успешно обновлён для пользователя ID {}. Файл: {}",
                    user.getId(), safeFilename);

        } catch (EntityNotFoundException e) {
            log.error("Пользователь не найден ID: {}. Ошибка: {}", user.getId(), e.getMessage());
            throw new UserNotFoundException("Пользователь не найден", e);
        } catch (IOException e) {
            log.error("Ошибка ввода-вывода при сохранении аватара для пользователя ID {}: {}",
                    user.getId(), e.getMessage());
            throw new FileStorageException("Ошибка при сохранении файла", e);
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обновлении аватара для пользователя ID {}: {}",
                    user.getId(), e.getMessage());
            throw new ServiceException("Ошибка при обновлении аватара", e);
        }

        }
    private static String getFileExtension(String filename) {
        // Находим индекс последней точки в имени файла
        int dotIndex = filename.lastIndexOf('.');

        // Если точка не найдена или находится в начале/конце имени - ошибка
        if (dotIndex <= 0 || dotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("Файл не имеет расширения или имя некорректно");
        }

        // Возвращаем подстроку после последней точки
        return filename.substring(dotIndex + 1);
    }


}
