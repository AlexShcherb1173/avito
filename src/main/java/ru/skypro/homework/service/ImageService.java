package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String getExtension(MultipartFile file);

    String saveImageToDisk(MultipartFile file, String username) throws IOException;

}
