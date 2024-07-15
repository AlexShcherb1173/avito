package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.UserNotFoundException;

public interface ImageService {
    Image addImage(MultipartFile image);

    byte[] getImageData(long id);
    Image getImage(long id);
    String getAdImageUrl(int adId);
    String getUserImageUrl(int userId) throws UserNotFoundException;
}
