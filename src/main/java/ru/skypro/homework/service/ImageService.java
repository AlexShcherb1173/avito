package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile image, String subDirectory, String begin) throws IOException;

    byte[] getImage(String filename, String subDirectory) throws IOException;

    String getImageContentType(String filename);

    boolean deleteImage(String filename, String subDirectory) throws IOException;

    void validateImageFile(MultipartFile file);

}
