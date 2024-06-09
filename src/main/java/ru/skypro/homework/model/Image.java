package ru.skypro.homework.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class Image extends ParentIDEntity {

    private long fileSize;

    private String mediaType;

    private byte[] data;

    @Builder
    public Image(Long id, long fileSize, String mediaType, byte[] data) {
        super(id);
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }

}