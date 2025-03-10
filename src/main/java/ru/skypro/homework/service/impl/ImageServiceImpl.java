package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl  implements ImageService{

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {

        try (InputStream is = file.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(baos, 1024);
        ) {
            bis.transferTo(bos);

            Image saveImage = new Image();
            saveImage.setData(baos.toByteArray());
            saveImage.setMediaType(file.getContentType());

            return imageRepository.save(saveImage);
        }
    }

    @Override
    public Image getImage(Long imageId) throws IOException {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Изображение не найдено"));

    }

    @Override
    public byte[] getImageData(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Изображение не найдено"));

        return image.getData();
    }
}