package ru.skypro.homework.entity;

import lombok.Data;

@Data
public class ModelEntity {

    public ru.skypro.homework.entity.PhotoEntity photo;
    private String filePath;
    private String image;
}