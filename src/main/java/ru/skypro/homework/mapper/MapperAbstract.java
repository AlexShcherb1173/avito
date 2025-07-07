package ru.skypro.homework.mapper;

import org.springframework.beans.factory.annotation.Value;

public abstract class MapperAbstract {
    @Value("${ads.image-uri}")
    private String imageUri;
    protected String getFullImageUrl(String imageFileName) {
        if (imageFileName == null) {
            return null;
        }
        return imageUri + "/" + imageFileName;
    }

}
