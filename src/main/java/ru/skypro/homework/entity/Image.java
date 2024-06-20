package ru.skypro.homework.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long fileSize; // Это поле содержит информацию о размере файла в байтах.
    private String mediaType; // Тип файла.
    private byte[] data; // В этом поле хранится сама информация о файле, представленная в массиве байтов
}
