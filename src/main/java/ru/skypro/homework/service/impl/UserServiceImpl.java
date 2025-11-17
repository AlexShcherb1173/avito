package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.user.NewPasswordDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageConfig fileStorageConfig;

    // Директория для хранения изображений
    private Path IMAGE_DIR;

    @PostConstruct
    public void init() {
        this.IMAGE_DIR = Paths.get(fileStorageConfig.getUploadDir(), "users").toAbsolutePath().normalize();
        log.info("Avatar storage location: {}", IMAGE_DIR);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto, String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        userMapper.updateEntityFromDto(updateUserDto, userEntity);
        UserEntity savedEntity = userRepository.save(userEntity);

        UpdateUserDto result = new UpdateUserDto();
        result.setFirstName(savedEntity.getFirstName());
        result.setLastName(savedEntity.getLastName());
        result.setPhone(savedEntity.getPhone());

        return result;
    }

    @Override
    public boolean updatePassword(NewPasswordDto newPasswordDto, String username) {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), userEntity.getPassword())) {
            return false;
        }

        userEntity.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public boolean updateUserImage(MultipartFile image, String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        validateImageFile(image);

        // Создаем директорию если не существует
        if (!Files.exists(IMAGE_DIR)) {
            Files.createDirectories(IMAGE_DIR);
            log.info("Created avatar directory: {}", IMAGE_DIR);
        }

        // Удаляем старое изображение если есть
        deleteOldUserImage(userEntity);

        // Генерируем уникальное имя файла
        String fileExtension = getFileExtension(image.getOriginalFilename());
        String fileName = generateFileName(fileExtension);

        // Сохраняем файл
        Path targetLocation = IMAGE_DIR.resolve(fileName);
        Files.copy(image.getInputStream(), targetLocation);

        // Обновляем поле в БД - сохраняем только имя файла
        userEntity.setImage(fileName);
        userRepository.save(userEntity);

        log.info("User image updated for user: {}, filename: {}", username, fileName);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getUserImage(String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (userEntity.getImage() == null || userEntity.getImage().isEmpty()) {
            throw new IOException("User has no image: " + username);
        }

        Path filePath = IMAGE_DIR.resolve(userEntity.getImage()).normalize();
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Image not found: " + filePath);
        }
        return Files.readAllBytes(filePath);
    }

    @Override
    public String getUserImageContentType(String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (userEntity.getImage() == null || userEntity.getImage().isEmpty()) {
            throw new IOException("User has no image: " + username);
        }

        return determineContentType(userEntity.getImage());
    }

    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".jpeg") || fileName.toLowerCase().endsWith(".jpg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }

    @Override
    public boolean deleteUserImage(String username) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        return deleteOldUserImage(userEntity);
    }

    private boolean deleteOldUserImage(UserEntity userEntity) throws IOException {
        if (userEntity.getImage() != null && !userEntity.getImage().isEmpty()) {
            //извлекаем имя файла из пути
            String currentFilename = userEntity.getImage();

            Path oldFilePath = IMAGE_DIR.resolve(currentFilename).normalize();
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
                log.info("Deleted old user image: {}", oldFilePath);
            }

            //Очищаем поле в БД
            userEntity.setImage(null);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }

    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        //проверка размера
        if (file.getSize() > fileStorageConfig.getAvatarMaxSize()) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size: " +
                    fileStorageConfig.getAvatarMaxSize() + " bytes");
        }

        //проверка типа содержимого
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(fileStorageConfig.getAvatarAllowedTypes())
                .contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: " +
                    Arrays.toString(fileStorageConfig.getAvatarAllowedTypes()));
        }
    }

    private String generateFileName(String extension){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);

        return "avatar_" + timestamp + (extension != null ? extension : "");
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ".jpg";  //расширение по умолчанию
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
