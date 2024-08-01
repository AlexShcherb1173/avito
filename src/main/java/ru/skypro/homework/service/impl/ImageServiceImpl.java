package ru.skypro.homework.service.impl;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.service.ImageService;

public class ImageServiceImpl implements ImageService {
    @Override
    public Image addImage(MultipartFile image) {
        return null;
    }

    @Override
    public byte[] getImageData(long id) {
        return new byte[0];
    }

    @Override
    public Image getImage(long id) {
        return null;
    }

    @Override
    public String getAdImageUrl(int adId) {
        return "";
    }

    @Override
    public String getUserImageUrl(int userId) throws UserNotFoundException {
        return "";
    }
}
