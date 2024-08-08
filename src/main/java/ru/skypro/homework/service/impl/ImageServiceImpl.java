package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private  UserRepository userRepository;

    @Override
    public ResponseEntity<byte[]> getImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(RuntimeException::new);
        byte[] imageBytes = image.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(imageBytes.length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(imageBytes);
    }

    @Override
    public Image createImage(MultipartFile image) {
        Image newImage = new Image();
        try {
            newImage.setData(image.getBytes());
            newImage.setMediaType(image.getContentType());
            newImage.setFileSize(image.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageRepository.save(newImage);
        return newImage;
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

}
