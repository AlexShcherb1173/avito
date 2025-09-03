package ru.skypro.homework.service;

public interface ImageService {
    void updateUserImage(byte[] image);
    void updateAdImage(Integer adId, byte[] image);
}
