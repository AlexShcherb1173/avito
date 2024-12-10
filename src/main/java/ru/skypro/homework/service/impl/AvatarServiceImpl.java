
package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.control.MappingControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.AvatarEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.Paths.get;


@Slf4j
@Service
public class AvatarServiceImpl implements AvatarService {
    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final Path path;

    public AvatarServiceImpl(UserRepository userRepository,
                             AvatarRepository avatarRepository,
                             @Value("/images") String imagesDirName) {
        this.userRepository = userRepository;
        this.avatarRepository = avatarRepository;
        path = get(imagesDirName);
    }



    /**
     * Сохранеет или меняет аватарку у пользователя.
     */
    @Override
    public void updateImage(MultipartFile multipartFile) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        UserEntity user = userRepository.findByEmail(userName).orElseThrow(() -> {
            log.info("Пользователь не найден", UserNotFoundException.class);
            return new UserNotFoundException("not");
        });

        AvatarEntity avatar = new AvatarEntity();

        if (user.getAvatar() != null) {
            avatar = user.getAvatar();
        }

        try {
            byte[] data = multipartFile.getBytes();
            String extention = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path imagePath = path.resolve(user.getId() + "." + extention);
            Files.write(imagePath, data);

            avatar.setPath(imagePath.toString());
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFileSize(multipartFile.getSize());
            avatar.setUser(user);
            avatarRepository.save(avatar);
            user.setAvatar(avatar);
            userRepository.save(user);
        } catch (IOException e) {
            throw new NullPointerException();
        }
    }

    /**
     * Возращает аватар в виде массива байт.
     */
    @Override
    public ResponseEntity<byte[]> getAvatar() throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        UserEntity user = userRepository.findByEmail(userName).orElseThrow(() -> {
            log.info("Пользователь не найден", UserNotFoundException.class);
            return new UserNotFoundException("not");

        });

        String path = user.getAvatar().getPath();
        byte[] image = Files.readAllBytes(get(path));
        return ResponseEntity.ok(image);
    }

}
