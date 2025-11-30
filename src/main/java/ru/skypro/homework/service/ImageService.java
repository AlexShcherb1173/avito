package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    String saveImage(MultipartFile image);
    byte[] getImage(String filename);
    void deleteImage(String filename);
}