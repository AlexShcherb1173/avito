package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final UserMapper userMapper;

    public UsersServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.userMapper = userMapper;
    }

    @Override
    public void setPassword(NewPasswordDto newPasswordDto) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Находим пользователя по email
        Optional<User> optionalUser = (userRepository.findByEmail(email));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException();
        }

        User user = optionalUser.get();

        // Проверяем, совпадает ли текущий пароль
        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException();

        }

        // Устанавливаем новый пароль
        user.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(user);

    }

    @Override
    public UserDto getUser() {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = (userRepository.findByEmail(username)).orElseThrow();
        log.info("-- getUser {}", user.getFirstName());
        // Преобразуем User в UserDto
        return userMapper.toUserDto(user);
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTODto) {

        String username = objectAuthentication();
        User user = (userRepository.findByEmail(username)).orElseThrow();
        User updateUser = userMapper.toUser(updateUserDTODto, user);
        userRepository.save(updateUser);
    }

    @Override
    public void uploadImage(MultipartFile file) throws IOException {
        log.info("@@@ uploadImage");
        String username = objectAuthentication();
        User user = (userRepository.findByEmail(username)).orElseThrow();
        user.setImage(imageService.uploadImage(file));
        log.info("uploadImage {}", user);
        userRepository.save(user);
    }

    private String objectAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }


}