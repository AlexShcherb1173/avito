package ru.skypro.homework.service;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

public interface ImageService {
    void updateUserImage(byte[] image, Authentication authentication);
    void updateAdImage(Integer adId, byte[] image);
}
