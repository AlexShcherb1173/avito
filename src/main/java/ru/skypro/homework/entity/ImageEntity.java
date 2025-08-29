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
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MEDIA_TYPE", nullable = false)
    private String mediaType;

    @Lob
    @Column(name = "DATA", nullable = false)
    private byte[] data;





}
