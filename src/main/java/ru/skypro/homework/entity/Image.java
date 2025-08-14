package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;  // Путь к файлу на диске (например: "users/avatar_123.jpg")

    @Column(name = "media_type", nullable = false, length = 50)
    private String mediaType; // MIME-тип (например: "image/jpeg")

    @Column(name = "file_size", nullable = false)
    private Long fileSize;    // Размер файла в байтах
}