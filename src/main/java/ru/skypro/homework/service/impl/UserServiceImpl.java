package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.LoginDTO;
import ru.skypro.homework.dto.RegisterDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exceptions.UnauthorizedException;
import ru.skypro.homework.mapper.UserDTOMapper;
import ru.skypro.homework.model.NewPassword;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exceptions.UserAlreadyExistsException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.model.UserAvatar;
import ru.skypro.homework.repository.UserAvatarRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;

@Slf4j
@Service
public class UserServiceImpl {
    @Autowired
    AvitoUserDetailsService avitoUserDetailsService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserAvatarRepository userAvatarRepository;
    @Autowired
    PasswordEncoder encoder;
    @Value("${my.dir}")
    private String pathDir;

    public Long createUser(RegisterDTO registerDTO) {
        if (registerDTO.getUsername() != null && //дописать
                userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем " + registerDTO.getUsername() + " уже существует");
        }

        return userRepository.save(new User(
                        registerDTO.getUsername(),
                        encoder.encode(registerDTO.getPassword()),
                        registerDTO.getFirstName(),
                        registerDTO.getLastName(),
                        registerDTO.getPhone(),
                        registerDTO.getRole()
                ))
                .getId();
    }

    public void setPassword(NewPassword newPassword, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new WrongPasswordException(username);
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        log.info("Пароль изменен");
    }

    public UserDTO showUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return UserDTOMapper.INSTANCE.userToAllInfoUserDTO(user);
    }

    public void updateUserInfo(String username, UserDTO userDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
    }

    public void uploadAvatar(LoginDTO loginDTO, MultipartFile multipartFile) throws IOException {
        createDirectory();
        Path filePath;
        if ((multipartFile.getOriginalFilename() != null)) {
            filePath = Path.of(pathDir, String.format("user(%s)", loginDTO) + "." +
                    getExtension(multipartFile.getOriginalFilename()));
            createAvatar(loginDTO, filePath.toString(), multipartFile);
            multipartFile.transferTo(filePath);
        }
    }


    public void createAvatar(LoginDTO loginDTO, String filePath, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException(loginDTO.getUsername()));
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
}
