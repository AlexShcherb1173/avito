package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Сервисный класс для управления пользователями.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final ImageService imageService;

    public UserServiceImpl(ImageRepository imageRepository,
                           UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           @Qualifier("userImageService") ImageService imageService) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.userMapper = userMapper;
        this.encoder = passwordEncoder;
        this.imageService = imageService;
    }


    /**
     * Устанавливает новый пароль для пользователя.
     *
     * <p>Находит пользователя по email (из {@code authentication}),
     * кодирует новый пароль и сохраняет его в базе данных.</p>
     *
     * @param newPasswordDTO DTO с новым паролем
     * @param authentication объект аутентификации, содержащий email пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public void updatePassword(NewPasswordDTO newPasswordDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
        userRepository.save(user);
    }


    /**
     * Получает информацию о текущем пользователе.
     *
     * <p>Находит пользователя по email (из {@code authentication}),
     * преобразует его в DTO и добавляет ссылку на изображение, если оно есть.</p>
     *
     * @param authentication объект аутентификации, содержащий email пользователя
     * @return DTO с информацией о пользователе
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserDTO getUserInfo(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UserNotFoundException(authentication.getName()));
        UserDTO userDTO = userMapper.toDtoUserDTO(user);
        if (userDTO.getImage() != null) {
            userDTO.setImage("/users/me/image/" + userDTO.getImage() + "/get");
        }
        return userDTO;
    }


    /**
     * Обновляет информацию о пользователе.
     *
     * <p>Обновляет имя, фамилию и телефон пользователя на основе переданных данных.</p>
     *
     * @param updateUserDTO  DTO с новыми данными пользователя
     * @param authentication объект аутентификации, содержащий email пользователя
     * @return обновленный {@link UpdateUserDTO}
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setFirstName(updateUserDTO.getFirstName());
        user.setPhone(updateUserDTO.getPhone());
        user.setLastName(updateUserDTO.getLastName());
        userRepository.save(user);
        return userMapper.toDtoUpdateUserDTO(user);
    }


    /**
     * Обновляет аватар пользователя.
     *
     * <p>Находит пользователя по email (из {@code authentication}), добавляет новое изображение
     * или заменяет старое, если оно уже есть.</p>
     *
     * @param image          новое изображение пользователя
     * @param authentication объект аутентификации, содержащий email пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     * @throws IOException               если произошла ошибка при сохранении изображения
     */
    @Override
    public void updateUserAvatar(MultipartFile image, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        try {
            Image imageObj = imageService.addImage(user.getId(), image);
            user.setImage(imageObj);
            userRepository.save(user);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    /**
     * Загружает аватар пользователя из файловой системы и отправляет его в HTTP-ответ.
     *
     * <p>Находит изображение по ID, считывает его из файловой системы и передает в выходной поток.</p>
     *
     * @param id       ID изображения пользователя
     * @param response HTTP-ответ, в который записывается изображение
     * @throws IOException               если произошла ошибка при чтении файла
     * @throws UsernameNotFoundException если изображение не найдено
     */
    public void downloadAvatarFromFileSystem(int id, HttpServletResponse response)
            throws IOException {

        Image avatarOpt = imageRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        Path path = Path.of(avatarOpt.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatarOpt.getMediaType());
            response.setContentLength((int) avatarOpt.getFileSize());
            is.transferTo(os);
        }
    }
}
