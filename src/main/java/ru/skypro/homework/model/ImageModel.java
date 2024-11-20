package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_model")
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId; // id изображения

    private String filePath; // Путь файла
    private long fileSize; // Размер файла в байтах
    private String mediaType; // Тип медиа (например, image/jpeg, image/png и т. д.).

    @Lob
    private byte[] data; // Данные изображения

    @OneToOne(mappedBy = "imageModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdModel adModel; // Связь с моделью объявления
}
