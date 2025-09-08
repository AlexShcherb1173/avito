package ru.skypro.homework.service;


import org.springframework.security.core.Authentication;

public interface ImageService {
    void updateUserImage(byte[] image, Authentication authentication);
    void updateAdImage(Integer adId, byte[] image);
}
