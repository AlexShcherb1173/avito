package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.utils.WorkWithFilesUtils;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Value(value = "${photo.dir.path}")
    private String photoDir;
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public byte[] getImage(int id) {
        Optional<ImageEntity> imageEntityOptional = imageRepository.findById(id);
        if (imageEntityOptional.isEmpty()) {
            return null;
        }
        ImageEntity imageEntity = imageEntityOptional.get();
        byte[] res = WorkWithFilesUtils.getImage(imageEntity.getNameImage(), photoDir);
        return res;
    }
}
