package ru.skypro.homework.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

public interface ImageService {

    public ResponseEntity<byte[]> getImage(Long id);

    public Image createImage(MultipartFile image);

    public void deleteImage(Long id);
}
