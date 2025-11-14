package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelEntity {

    public ru.skypro.homework.entity.PhotoEntity photo;
    private String filePath;
    private String image;
}