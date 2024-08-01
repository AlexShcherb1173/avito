package ru.skypro.homework.dto.image;

import java.util.Objects;

public class ImageDTO {

    private Long id;

    private long fileSize;

    private String filePath;

    private String mediaType;

    public ImageDTO() {
        this.id = id;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDTO imageDTO = (ImageDTO) o;
        return fileSize == imageDTO.fileSize && Objects.equals(id, imageDTO.id) && Objects.equals(filePath, imageDTO.filePath) && Objects.equals(mediaType, imageDTO.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileSize, filePath, mediaType);
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "id=" + id +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
