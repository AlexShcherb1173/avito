package ru.skypro.homework.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class UserAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;
    @OneToOne
    private User user;

    public UserAvatar(String filePath, long fileSize, String mediaType, byte[] data, User user) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.user = user;
    }

    public UserAvatar() {
    }
}

