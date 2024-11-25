package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <b>Класс {@link  Image} представляет собой сущность изображения в системе. </b> <p>
 * Он содержит информацию о файле изображения, включая путь к файлу, размер и тип медиа.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    /**
     * <b>Уникальный идентификатор изображения. </b> <p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * <b>Путь к файлу изображения. </b> <p>
     */
    private String filePath;

    /**
     * <b>Размер файла изображения в байтах.</b> <p>
     */
    private long fileSize;

    /**
     * <b>Тип медиа (например, "image/jpeg", "image/png"). </b> <p>
     */
    private String mediaType;

    /**
     * <b>Данные изображения в виде массива байтов. </b> <p>
     * Это поле игнорируется при сериализации в JSON.
     */
    @JsonIgnore
    private byte[] data;

}
