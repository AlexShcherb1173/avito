package ru.skypro.homework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Id
    private Integer id;

    @Column(name = "size") // размер файла
    private Long fileSize;

    @Column(name = "file_path") // путь к файлу
    private String filePath;

    @Column(name = "media_type") // тип медиа файла
    private String mediaType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) && Objects.equals(fileSize, image.fileSize) && Objects.equals(filePath, image.filePath) && Objects.equals(mediaType, image.mediaType) && Objects.deepEquals(data, image.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileSize, filePath, mediaType, Arrays.hashCode(data));
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "data")
    private byte[] data;
}
