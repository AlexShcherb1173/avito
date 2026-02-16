package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.AuthenticationFacade;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthenticationFacade authenticationFacade, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
        this.encoder = encoder;
    }

    public void updateUserImage(MultipartFile file) {
        User user = authenticationFacade.getCurrentUser();

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Файл не выбран или пустой");
            }

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new RuntimeException("Неверный формат изображения");
            }

            int targetWidth = 300;
            int targetHeight = 300;

            Image scaledInstance = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(scaledInstance, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, "jpg", baos);
            byte[] scaledBytes = baos.toByteArray();

            user.setImage(scaledBytes);
            userRepository.save(user);

            log.info("Изображение пользователя {} обновлено, новый размер: {} байт", user.getEmail(), scaledBytes.length);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения изображения", e);
        }
    }

    public byte[] getUserImage() {
        User user = authenticationFacade.getCurrentUser();
        return user.getImage();
    }

    public UserDTO getCurrentUserDto() {
        User user = authenticationFacade.getCurrentUser();
        UserDTO dto = UserMapper.INSTANCE.userToUserDto(user);

        if (user.getImage() != null) {
            dto.setImage("/users/me/image");
        }

        return dto;
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        UserDTO dto = UserMapper.INSTANCE.userToUserDto(user);

        if (user.getImage() != null) {
            dto.setImage("/users/" + user.getId() + "/image");
        }

        return dto;
    }

    public byte[] getUserImageById(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        return user != null ? user.getImage() : null;
    }

    public UpdateUser updateUser(UpdateUser updateUser) {
        User user = authenticationFacade.getCurrentUser();
        if (user != null) {
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setPhone(updateUser.getPhone());
            userRepository.save(user);
            return updateUser;
        } else {
            return null;
        }
    }

    public void changePassword (NewPassword newPassword){
        User user = authenticationFacade.getCurrentUser();
//        if(!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())){
//            throw new RuntimeException("Неверный текущий пароль");
//        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }


}
