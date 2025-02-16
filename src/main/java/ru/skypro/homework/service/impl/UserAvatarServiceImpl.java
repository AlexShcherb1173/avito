package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exceptions.AvatarNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.model.UserAvatar;
import ru.skypro.homework.repository.UserAvatarRepository;
import ru.skypro.homework.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;

@Slf4j
@Service
public class UserAvatarServiceImpl {
    @Autowired
    private UserAvatarRepository userAvatarRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${my.dir}")
    private String pathDir;
    public void updateAvatar(Principal principal, MultipartFile multipartFile) throws IOException {
        createDirectory();
        Path filePath;
        if ((multipartFile.getOriginalFilename() != null)) {
            filePath = Path.of(pathDir, String.format("user(%s)", principal.getName()) + "." +
                    getExtension(multipartFile.getOriginalFilename()));
            createAvatar(principal, filePath.toString(), multipartFile);
            multipartFile.transferTo(filePath);
        }
    }

    public void createAvatar(Principal principal, String filePath, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        log.info("Добавлен аватар пользователю {}", user.getUsername());
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


    public void transferImageToResponse(Long id, HttpServletResponse response) {
        log.info("Был вызван метод для трансформации изображения для ответа{}{}", id, response);
        UserAvatar userAvatar = userAvatarRepository.findById(id)
                .orElseThrow(() -> new AvatarNotFoundException("Не удалось найти изображение по id: " + id));
        try (OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(userAvatar.getMediaType());
            response.setContentLength((int) userAvatar.getFileSize());
            os.write(userAvatar.getData()); // Получаем данные изображения из базы
        } catch (IOException e) {
            throw new RuntimeException("Failed to transfer image to response ", e);
        }
    }
}