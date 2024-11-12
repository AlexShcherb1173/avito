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
public class ImageModel {

    @Id
    @GeneratedValue
    private Long imageId;

    private String filePath; //путь файла
    private long fileSize; //размер файла в байтах
    private String mediaType; //тип медиа (например, image/jpeg, image/png и т. д.).

    @Lob
    private byte[] data;

    @OneToOne(mappedBy = "imageModel")
    private AdModel adModel;
}
