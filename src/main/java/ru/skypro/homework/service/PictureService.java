package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.PictureOwner;

import java.io.IOException;
import java.nio.file.Path;

public interface PictureService {
    Picture uploadPicture(Integer ownerId, PictureOwner pictureOwner, MultipartFile file) throws IOException;

    byte[] generateImagePreview(Path filePath) throws IOException;

    void deleteByAdId(Integer adId);

    Picture findById(Integer id);
}
