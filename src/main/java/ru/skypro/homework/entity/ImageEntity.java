package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String mediaType;

    @Lob
    private byte[] data;

}
