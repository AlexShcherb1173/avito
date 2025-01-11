package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public UsersServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @Override
    public String setPassword(NewPasswordDto newPasswordDto) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Находим пользователя по email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Пользователь не найден";
        }

        User user = optionalUser.get();

        // Проверяем, совпадает ли текущий пароль
        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            return "Текущий пароль неверен";
        }

        // Устанавливаем новый пароль
        user.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(user);

        return "Пароль успешно изменен";
    }

    @Override
    public UserDto getUser() {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Находим пользователя по email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Пользователь не найден");
        }

        User user = optionalUser.get();

        // Преобразуем User в UserDto

        return toUserDto(user);

    }

    @Override
    public String updateUser(UserDto userDto) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Находим пользователя по email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Пользователь не найден";
        }

        User user = optionalUser.get();

        // Обновляем данные пользователя
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());

        userRepository.save(user);

        return "Данные пользователя успешно обновлены";
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Находим пользователя по email
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Пользователь не найден";
        }

        User user = optionalUser.get();

        // Загружаем изображение через ImageService
        Image image = imageService.uploadImage(file);

        // Обновляем изображение у пользователя
        user.setImage(image);
        userRepository.save(user);

        return "Изображение успешно загружено";
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setRoleDto(user.getRoleDto());

        // Устанавливаем URL изображения, если оно есть
        if (user.getImage() != null) {
            userDto.setImage(user.getImage().getPath()); // Используем getPath() вместо getUrl()
        }

        return userDto;
    }
}