package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * <b>Сервис для управления аватарами пользователей. </b> <p>
 * <p>
 * Этот класс реализует интерфейс {@link AvatarService} и предоставляет методы
 * для загрузки, получения и удаления аватаров.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    /**
     * <b>Путь к директории, где хранятся загруженные изображения. </b> <p>
     */
    public static String uploadDirectory = System.getProperty("user.dir") + "/images";

    private final AvatarRepository repository;
    private final UserRepository userRepository;

    /**
     * <b>Удаляет указанный аватар из репозитория. </b> <p>
     *
     * @param avatar аватар, который необходимо удалить.
     */
    @Override
    public void removeAvatar(Avatar avatar) {
        repository.delete(avatar);

    }

    /**
     * <b>Получает аватар по его идентификатору. </b> <p>
     *
     * @param id идентификатор аватара.
     * @return аватар с указанным идентификатором, или новый пустой аватар, если не найден.
     */
    @Override
    public Avatar getAvatar(Long id) {
        return repository.findById(id).orElse(new Avatar());
    }

    /**
     * <b>Загружает новый аватар для текущего пользователя. </b> <p>
     *
     * @param image файл изображения, который необходимо загрузить.
     * @return загруженный аватар.
     * @throws IOException если произошла ошибка при работе с файлами.
     */
    @Override
    public Avatar uploadAvatar(MultipartFile image) throws IOException {
        User user = userRepository.findUserByEmailIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Path filePath = Path.of(uploadDirectory,"user_" + user.getId() + UUID.randomUUID() + "." + getExtensions(Objects.requireNonNull(image.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(image.getSize());
        avatar.setMediaType(image.getContentType());
        avatar.setData(image.getBytes());
        repository.save(avatar);
        return avatar;
    }

    /**
     * <b>Получает расширение файла из его имени. </b> <p>
     *
     * @param fileName имя файла.
     * @return расширение файла без точки.
     */
    public String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
