package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.ImageService;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageServiceImpl extends ImageService {

    @Value("$path.to.photos.folder$")
    private String photosDir;

    @Value("$path.to.avatars.folder$")
    private String avatarsDir;

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Photo uploadPhoto(MultipartFile file) {
        try {
            Photo photo = new Photo(photosDir);
            mapFileToImage(file, photo);
            photo = imageRepository.save(photo);
            upload(photo, file);
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Avatar uploadAvatar(User user, MultipartFile file) {
        try {
            Avatar avatar = user.getAvatar();
            if (avatar == null) {
                avatar = new Avatar(avatarsDir);
            }
            mapFileToImage(file, avatar);
            avatar = imageRepository.save(avatar);
            upload(avatar, file);
            user.setAvatar(avatar);
            userRepository.save(user);
            return avatar;
        } catch (Exception e) {;
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deleteFile(Image image) {
        if (image != null) {
            try {
                Files.deleteIfExists(image.getFilePath().toAbsolutePath().toFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void upload(Image image, MultipartFile file) throws IOException {
        Path filePath = image.getFilePath();
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        Files.write(filePath, file.getBytes());
    }
    private void mapFileToImage(MultipartFile file, Image image) {
        image.setFileType(file.getContentType());
        image.setFileName(file.getOriginalFilename());
        image.setFileExtension(StringUtils.getFilenameExtension(file.getOriginalFilename()));
        image.setFileSize(file.getSize());
    }

}
