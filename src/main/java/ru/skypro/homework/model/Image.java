package ru.skypro.homework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность изображения (Image).
 */
@Setter
@Getter
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String filePath;

    private long fileSize;

    private String mediaType;

    public Image() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id
                && fileSize == image.fileSize
                && Objects.equals(filePath, image.filePath)
                && Objects.equals(mediaType, image.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
