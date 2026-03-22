package ru.avito.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String saveAdImage(Integer adId, MultipartFile file);

    String saveUserImage(Integer userId, MultipartFile file);

    void deleteImageIfExists(String imagePath);
}