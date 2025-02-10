package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdvert;
import ru.skypro.homework.model.Image;


import java.io.IOException;

public interface ImageService {
    Image uploadAdImage(CreateOrUpdateAdvert properties, MultipartFile image) throws IOException;

    Integer findImageIdByImagePath(String imagePath);

    String updateAdImage(Integer imageId, MultipartFile image) throws IOException;
}
