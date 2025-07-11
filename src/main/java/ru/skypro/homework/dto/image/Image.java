package ru.skypro.homework.dto.image;

import lombok.Data;

@Data
public class Image {
    private byte[] imageBytes;
    private String contentType;
}