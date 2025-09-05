package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

// Реализация сервиса для управления пользователями.
// Обеспечивает операции с профилем пользователя, включая обновление аватара и смену пароля.

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    // Получение DTO пользователя по данным аутентификации.
    // @param userDetails данные аутентификации пользователя
    // @return UserDto с информацией о пользователе
    // @throws RuntimeException если пользователь не найден

    @Override
    public UserDto getUserDto(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    // Обновление информации о пользователе.
    // @param dto данные для обновления
    // @param userDetails данные аутентификации пользователя
    // @return UserDto с обновленной информацией
    // @throws RuntimeException если пользователь не найден

    @Override
    public UserDto updateUser(UpdateUser dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        UserMapper.INSTANCE.updateUserFromDto(dto, user);
        User saved = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(saved);
    }

    // Обновление аватара пользователя.
    // Сохраняет изображение и возвращает URL для доступа к нему.
    // @param image файл изображения
    // @param userDetails данные аутентификации пользователя
    // @return String URL обновленного аватара
    // @throws IllegalArgumentException если файл пустой
    // @throws RuntimeException если пользователь не найден или произошла ошибка при сохранении изображения

    @Override
    public String updateUserImage(MultipartFile image, UserDetails userDetails) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        try {
            String filename = imageService.saveImage(image, "users");
            user.setImage(filename);
            userRepository.save(user);

            // ВАЖНО: Возвращаем полный URL для доступа к изображению
            return "/images/users/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    // Смена пароля пользователя с проверкой текущего пароля.
    // @param dto DTO с текущим и новым паролем
    // @param userDetails данные аутентификации пользователя
    // @throws BadCredentialsException если текущий пароль неверный
    // @throws IllegalArgumentException если новый пароль слишком короткий
    // @throws RuntimeException если пользователь не найден

    @Override
    public void setPassword(NewPassword dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Проверяем текущий пароль
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        // Проверяем длину нового пароля
        if (dto.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters");
        }

        // Шифруем и сохраняем новый пароль
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}