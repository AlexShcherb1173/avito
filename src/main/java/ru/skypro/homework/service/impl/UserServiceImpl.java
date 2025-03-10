package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.exception.UserAlreadyExistsException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.model.UserAvatar;
import ru.skypro.homework.repository.UserAvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    AuthServiceImpl authService;
    UserRepository userRepository;
    UserAvatarRepository userAvatarRepository;
    PasswordEncoder encoder;
    @Value("${my.dir}")
    private String pathDir;

    @Override
    public void createUser(Register register) {
        if (userRepository.findByUsername(register.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(register);
        } else {
            userRepository.save(new User(
                    register.getUsername(),
                    encoder.encode(register.getPassword()),
                    register.getFirstName(),
                    register.getLastName(),
                    register.getPhone(),
                    register.getRole()
            ));
        }
    }


    @Override
    public boolean setPassword(String userName, String password, String newPassword) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> {
                    log.error("Пользователь не найден: {}", userName);
                    return new UsernameNotFoundException("Пользователь не найден");
                });

        if (!encoder.matches(password, user.getPassword())) {
            log.error("Пароли не совпадают");
            throw new WrongPasswordException("Пароли не совпадают");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO showUserInfo(Login login) {
        checkLogin(login);

        User user = userRepository.findByUsername(login.getUsername()).orElseThrow();

        return UserMapper.INSTANCE.userToInfoUserDTO(user);
    }

    @Override
    public void updateUserInfo(Login login, String firstName, String lastName, String phone) {
        checkLogin(login);

        User user = userRepository.findByUsername(login.getUsername()).orElseThrow();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        userRepository.save(user);
    }

    @Override
    public void uploadAvatar(Login login, MultipartFile multipartFile) throws IOException {
        checkLogin(login);

        createDirectory();

        Path filePath;
        if ((multipartFile.getOriginalFilename() != null)) {
            filePath = Path.of(pathDir, String.format("user(%s)", login) + "." +
                    getExtension(multipartFile.getOriginalFilename()));

            createAvatar(login, filePath.toString(), multipartFile);

            multipartFile.transferTo(filePath);
        }
    }

    @Override
    public void createAvatar(Login login, String filePath, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByUsername(login.getUsername()).orElseThrow(() -> new UsernameNotFoundException(login.getUsername()));
        userAvatarRepository.save(new UserAvatar(
                filePath,
                multipartFile.getSize(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                user));
    }

    private String getExtension(String originalPath) {
        return originalPath.substring(originalPath.lastIndexOf(".") + 1);
    }

    void createDirectory() throws IOException {
        Path path = Path.of(pathDir);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }

    public void checkLogin(Login login) {
        if (!authService.login(login.getUsername(), login.getPassword())) {
            log.error("Пользователь не авторизован: {}", login.getUsername());
            throw new UnauthorizedException(login);
        }
    }
}
