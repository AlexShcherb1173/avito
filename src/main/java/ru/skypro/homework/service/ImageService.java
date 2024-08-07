package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.User;

public abstract class ImageService {
    public abstract Photo uploadPhoto(MultipartFile file);

    public abstract Avatar uploadAvatar(User user, MultipartFile file);

    public abstract void deleteFile(Image image);
}
