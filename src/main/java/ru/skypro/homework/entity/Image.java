package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long fileSize;
    private String mediaType;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;
}

