package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveImage(MultipartFile file) throws IOException;

    byte[] getImage(String fileName) throws IOException;
}